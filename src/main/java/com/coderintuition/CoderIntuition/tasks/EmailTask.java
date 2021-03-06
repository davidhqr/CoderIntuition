package com.coderintuition.CoderIntuition.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.coderintuition.CoderIntuition.common.Utils;
import com.coderintuition.CoderIntuition.config.AppProperties;
import com.coderintuition.CoderIntuition.models.Problem;
import com.coderintuition.CoderIntuition.models.User;
import com.coderintuition.CoderIntuition.repositories.ProblemRepository;
import com.coderintuition.CoderIntuition.repositories.UserRepository;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailTask {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private AppProperties appProperties;

    // send email every morning at 9am Eastern
    // @Scheduled(cron = "0 * * * * *", zone = "America/New_York")
    @Scheduled(cron = "0 0 9 * * *", zone = "America/New_York")
    public void sendEmails() {
        List<User> emailableUsers = userRepository.findEmailableUsers();
        for (User user : emailableUsers) {
            log.info(user.getEmail());

            // make sure last email was at least 23 hours ago
            if (user.getLastEmailSentAt() != null) {
                Date yest = DateUtils.addHours(user.getLastEmailSentAt(), -23);
                if (user.getLastEmailSentAt().after(yest)) {
                    log.warn("Attempted to send another email within 23 hours, userId={}", user.getId());
                    // return;
                }
            }

            // get sent problems
            List<Long> problemsSent = getProblemsSent(user);

            List<Problem> unsentProblems = problemsSent.isEmpty() ? problemRepository.findAll()
                    : problemRepository.findUnsentProblems(problemsSent);
            if (unsentProblems.isEmpty()) {
                // reset problems sent
                log.info("Resetting problemsSent, userId={}", user.getId());
                user.setProblemsSent(new JSONArray());
                userRepository.save(user);
                problemsSent = getProblemsSent(user);
                unsentProblems = problemRepository.findAll();
            }

            Problem problemToSend = unsentProblems.get(new Random().nextInt(unsentProblems.size()));

            // build email
            MutableDataSet options = new MutableDataSet();
            options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();

            Node document = parser.parse(problemToSend.getDescription());
            String html = "<div class='markdown-body'>" + renderer.render(document) + "</div>";

            // set uuid if user doesn't have one yet
            if (user.getUuid() == null) {
                user.setUuid(UUID.randomUUID().toString());
                userRepository.save(user);
            }

            String dailyProblemEmail = Utils.fileToString("email/dailyproblem.html")
                    .replaceAll("\\{\\{name}}", Utils.getFirstName(user.getName()))
                    .replaceAll("\\{\\{urlName}}", problemToSend.getUrlName())
                    .replaceAll("\\{\\{problemName}}", problemToSend.getName())
                    .replaceAll("\\{\\{uuid}}", user.getUuid())
                    .replaceAll("\\{\\{description}}", html);

            // send email
            Utils.sendEmail(appProperties.getMailgun().getKey(), user.getEmail(),
                    "Daily CoderIntuition Problem: " + problemToSend.getName(), dailyProblemEmail);

            log.info("Emailing daily problem, userId={}, problemId={}", user.getId(), problemToSend.getId());

            // update sent information
            problemsSent.add(problemToSend.getId());
            user.setProblemsSent(new JSONArray(problemsSent));
            user.setLastEmailSentAt(new Date());
            userRepository.save(user);
        }
    }

    private List<Long> getProblemsSent(User user) throws JSONException {
        List<Long> problemsSent = new ArrayList<>();
        JSONArray jsonArray = user.getProblemsSent();
        for (int i = 0; i < jsonArray.length(); i++) {
            problemsSent.add(jsonArray.getLong(i));
        }
        return problemsSent;
    }
}
