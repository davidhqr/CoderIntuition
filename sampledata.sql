REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (1, 'Sort Array', 'sort-array', 'arrays', 1, 2, 40,
        'Given an array of integers, sort the array in non-decreasing order.',
        'def sort_array(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem_step (id, problem_id, step_num, name, type, text_contents, quiz_contents, time, created_at, updated_at)
VALUES (1, 1, 1, 'Step 1: Understand the Problem', 'text', 'Read the problem and understand it.', '', 5, '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem_step (id, problem_id, step_num, name, type, text_contents, quiz_contents, time, created_at, updated_at)
VALUES (2, 1, 2, 'Step 2: Problem Understanding Quiz', 'quiz', '', '{
	"quizTitle": "",
	"quizSynopsis": "",
	"appLocale": {
		"landingHeaderText": "",
		"question": "Question",
		"startQuizBtn": "Start Quiz",
		"resultFilterAll": "All",
		"resultFilterCorrect": "Correct",
		"resultFilterIncorrect": "Incorrect",
		"nextQuestionBtn": "Next",
		"resultPageHeaderText": "Results:"
	},
	"questions": [{
			"question": "How can you access the state of a component from inside of a member function?",
			"questionType": "text",
			"answerSelectionType": "single",
			"answers": [
				"this.getState()",
				"this.prototype.stateValue",
				"this.state",
				"this.values"
			],
			"correctAnswer": "3",
			"messageForCorrectAnswer": "Correct answer. Good job.",
			"messageForIncorrectAnswer": "Incorrect answer. Please try again.",
			"explanation": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
			"point": "1"
		},
		{
			"question": "ReactJS is developed by _____?",
			"questionType": "text",
			"answerSelectionType": "single",
			"answers": [
				"Google Engineers",
				"Facebook Engineers"
			],
			"correctAnswer": "2",
			"messageForCorrectAnswer": "Correct answer. Good job.",
			"messageForIncorrectAnswer": "Incorrect answer. Please try again.",
			"explanation": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
			"point": "1"
		}
	]
}', 5, '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.testcase(id, problem_id, testcase_num, name, is_default, input, output, time_limit, memory_limit, stack_limit, created_at, updated_at)
VALUES (1, 1, 1, 'Test 1', true, '[1, 2, 3, 4]', '[1, 2, 3, 4]', 10, 10, 10, '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.testcase(id, problem_id, testcase_num, name, is_default, input, output, time_limit, memory_limit, stack_limit, created_at, updated_at)
VALUES (2, 1, 2, 'Test 1', false, '[3, 1, 4, 2]', '[1, 2, 3, 4]', 10, 10, 10, '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.solution(id, problem_id, solution_num, name, is_primary, code, description, created_at, updated_at)
VALUES (1, 1, 1, 'Approach 1: Manually sort the array', false, 'def sort_array(nums):\n    return []', 'Go through each element and compare', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.solution(id, problem_id, solution_num, name, is_primary, code, description, created_at, updated_at)
VALUES (2, 1, 2, 'Approach 2: Use built-in sorted', true, 'def sort_array(nums):\n    return sorted(nums)', 'Use the built-in function', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (2, 'Maximum Subarray', 'maximum-subarray', 'arrays', 1, 3, 30,
        'Given an array of integers return the contiguous subarray that has the largest sum',
        'def max_subarray(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (3, 'Subsets', 'subsets', 'arrays', 3, 5, 69,
        'Given a set of distinct integers, nums, return all possible subsets (the power set).',
        'def subsets(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (4, 'Move Zeros', 'move-zeros', 'arrays', 2, 7, 14,
        'Given an array nums, write a function to move all 0''s to the end of it while maintaining the relative order of the non-zero elements.',
        'def move_zeros(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (5, 'Product of Array Except Self', 'product-of-array-except-self', 'arrays', 4, 12, 47,
        'Given an array nums of n integers where n > 1,  return an array output such that output[i] is equal to the product of all the elements of nums except nums[i].',
        'def product_of_array_except_self(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (6, 'Trapping Rain Water', 'trapping-rain-water', 'arrays', 5, 21, 71,
        'Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it is able to trap after raining.',
        'def trapping_rain_water(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (7, 'Majority Element', 'majority-element', 'arrays', 2, 8, 38,
        'Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊ n/2 ⌋ times.',
        'def majority_element(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (8, 'Contains Duplicate', 'contains-duplicate','arrays', 1, 9, 16,
        'Given an array of integers, find if the array contains any duplicates.',
        'def contains_duplicate(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (9, 'Pascals Triangle', 'pascals-triangle', 'arrays', 3, 39, 67,
        'Given a non-negative integer numRows, generate the first numRows of Pascals triangle.',
        'def pascals_triangle(nums):', '2020-08-29 00:00:00', '2020-08-29 00:00:00');

REPLACE INTO coderintuition.problem (id, name, url_name, category, difficulty, num_steps, total_time, description, default_code, created_at,
                      updated_at)
VALUES (10, 'Word Search', 'word-search', 'arrays', 4, 8, 30,
        'Given a 2D board and a word, find if the word exists in the grid.',
        'def exist(self, board: List[List[str]], word: str) -> bool:', '2020-08-29 00:00:00', '2020-08-29 00:00:00');