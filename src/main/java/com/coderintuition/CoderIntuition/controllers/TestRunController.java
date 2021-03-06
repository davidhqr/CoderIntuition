package com.coderintuition.CoderIntuition.controllers;

import com.coderintuition.CoderIntuition.common.CodeTemplateFiller;
import com.coderintuition.CoderIntuition.common.Constants;
import com.coderintuition.CoderIntuition.common.Utils;
import com.coderintuition.CoderIntuition.config.AppProperties;
import com.coderintuition.CoderIntuition.enums.TestStatus;
import com.coderintuition.CoderIntuition.models.Problem;
import com.coderintuition.CoderIntuition.models.Solution;
import com.coderintuition.CoderIntuition.models.TestRun;
import com.coderintuition.CoderIntuition.pojos.request.JZSubmissionRequestDto;
import com.coderintuition.CoderIntuition.pojos.request.ProduceOutputDto;
import com.coderintuition.CoderIntuition.pojos.request.RunRequestDto;
import com.coderintuition.CoderIntuition.pojos.response.JzSubmissionCheckResponse;
import com.coderintuition.CoderIntuition.pojos.response.ProduceOutputResponse;
import com.coderintuition.CoderIntuition.pojos.response.TestRunResponse;
import com.coderintuition.CoderIntuition.pojos.response.TokenResponse;
import com.coderintuition.CoderIntuition.repositories.ProblemRepository;
import com.coderintuition.CoderIntuition.repositories.TestRunRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Slf4j
@Controller
@RestController
public class TestRunController {

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    TestRunRepository testRunRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    AppProperties appProperties;

    @PutMapping("/testrun/judge0callback")
    public void testRunCallback(@RequestBody JzSubmissionCheckResponse data) throws Exception {
        log.info("PUT /testrun/judge0callback, data={}", data.toString());

        // get test run info
        JzSubmissionCheckResponse result = Utils.retrieveFromJudgeZero(data.getToken(), appProperties);
        log.info("result={}", result.toString());

        // wait until test run is written to db from createTestRun
        await().atMost(15, SECONDS).until(() -> testRunRepository.findByToken(result.getToken()).isPresent());
        log.info("Done waiting for test run to be written to db");

        // fetch the test run from the db
        TestRun testRun = testRunRepository.findByToken(result.getToken()).orElseThrow();

        // save the results of the test run
        if (result.getStatus().getId() >= 6) { // error
            testRun.setStatus(TestStatus.ERROR);
            testRun.setExpectedOutput("");
            testRun.setOutput("");
            String stderr = "";
            if (result.getCompileOutput() != null) {
                stderr = result.getCompileOutput();
            } else if (result.getStderr() != null) {
                stderr = result.getStderr();
            }
            testRun.setStderr(Utils.formatErrorMessage(testRun.getLanguage(), stderr));
            testRun.setStdout("");

        } else if (result.getStatus().getId() == 3) { // no compile errors
            // everything above the line is stdout, everything below is test results
            String[] split = result.getStdout().trim().split(Constants.IO_SEPARATOR);
            String[] testResult = split[1].split("\\|");
            log.info("testResult={}", Arrays.toString(testResult));

            if (testResult.length == 3) { // no errors
                // test results are formatted: {status}|{expected output}|{run output}
                testRun.setStatus(TestStatus.valueOf(testResult[0]));
                testRun.setExpectedOutput(testResult[1]);
                testRun.setOutput(testResult[2]);
                testRun.setStdout(split[0]);
                testRun.setStderr("");

            } else if (testResult.length == 2) { // runtime errors
                // runtime error results are formatted: {status}|{error message}
                testRun.setStatus(TestStatus.ERROR);
                testRun.setExpectedOutput("");
                testRun.setOutput("");
                testRun.setStderr(Utils.formatErrorMessage(testRun.getLanguage(), testResult[1]));
                testRun.setStdout("");
            }
        }

        // send message to frontend
        TestRunResponse testRunResponse = TestRunResponse.fromTestRun(testRun);
        this.simpMessagingTemplate.convertAndSend(
            "/global/" + testRun.getSessionId() + "/testrun",
            testRunResponse
        );
        log.info(
            "Sent test run over websocket, destination=/global/{}/testrun, testRunResponse={}",
            testRun.getSessionId(),
            testRunResponse
        );

        // save the test run into the db
        testRunRepository.save(testRun);
    }

    @MessageMapping("/global/{sessionId}/testrun")
    public void createTestRun(@DestinationVariable String sessionId, Message<RunRequestDto> message) throws Exception {
        RunRequestDto runRequestDto = message.getPayload();
        log.info("Received websocket message, destination=/global/{}/testrun, runRequestDto={}", sessionId, runRequestDto.toString());

        // retrieve the problem
        Problem problem = problemRepository.findById(runRequestDto.getProblemId()).orElseThrow();

        // wrap the code into the test run template
        CodeTemplateFiller filler = CodeTemplateFiller.getInstance();
        String functionName = Utils.getFunctionName(runRequestDto.getLanguage(), problem.getCode(runRequestDto.getLanguage()));
        String primarySolution = problem.getSolutions().stream().filter(Solution::getIsPrimary).findFirst().orElseThrow().getCode(runRequestDto.getLanguage());

        // fill in the test run template with the arguments/return type for this test run
        String code = filler.getTestRunCode(runRequestDto.getLanguage(), runRequestDto.getCode(), primarySolution,
            functionName, problem.getOrderedArguments(), problem.getReturnType());
        log.info("Generated test run code from template, code={}", code);

        // create request to JudgeZero
        JZSubmissionRequestDto jzSubmissionRequestDto = new JZSubmissionRequestDto();
        jzSubmissionRequestDto.setSourceCode(code);
        jzSubmissionRequestDto.setLanguageId(Utils.getLanguageId(runRequestDto.getLanguage()));
        jzSubmissionRequestDto.setStdin(runRequestDto.getInput());
        jzSubmissionRequestDto.setCallbackUrl(appProperties.getJudge0().getCallbackUrl() + "/testrun/judge0callback");

        // send request to JudgeZero
        String token = Utils.submitToJudgeZero(jzSubmissionRequestDto, appProperties);
        log.info("Submitted test run to JudgeZero, token={}, jzSubmissionRequestDto={}", token, jzSubmissionRequestDto.toString());

        // create the test run to be saved into the db
        TestRun testRun = new TestRun();
        testRun.setProblem(problem);
        testRun.setSessionId(runRequestDto.getSessionId());
        testRun.setUserId(runRequestDto.getUserId());
        testRun.setToken(token);
        testRun.setLanguage(runRequestDto.getLanguage());
        testRun.setCode(runRequestDto.getCode());
        testRun.setInput(runRequestDto.getInput());
        testRun.setStatus(TestStatus.RUNNING);
        testRunRepository.save(testRun);
        log.info("Saved test run to database, testRun={}", testRun);
    }
}
