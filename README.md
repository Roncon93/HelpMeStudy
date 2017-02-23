# HelpMeStudy
Java program to help people study for tests.

## How To Use
In order to load a test, simply create a text file containing the test data.

The data must be in JSON format and each question must have the following fields:
⋅⋅* **question**: The content of the question.
⋅⋅* **type**: The type of question (**NOTE**: For now only multiple choice questions are supported, leave this value as 0).
⋅⋅* **answers**: An array containing the possible answers.
⋅⋅* **correct_answer**: The index of the correct answer.

Also make sure the questions are inside a JSON array.

### Example Test File
[
	{
		"question":"What is 1 + 1?",
		"type":0,
		"answers":["1","0","∞","2"],
		"correct_answer":3
	}
]

To load the test data file, insert the file in the root directory of the project and change the value of FILENAME in TesterController.java to the name of the file (be sure the file format, if any, is included in the string).
