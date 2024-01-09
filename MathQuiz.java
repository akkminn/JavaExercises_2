import java.util.Scanner;

/**
 * Main program to ask user 10 simple random math questions.
 */
public class MathQuiz {

	/**
	 * Main method to execute the math quiz program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("What's your name? ");
		String name = scanner.nextLine();

		System.out.println("Pleased to meet you, " + name);
		System.out.println("Let's dive into the quiz world!!");

		int totalProblems = 10; 
		int totalScore = 0;
		int firstTryCorrect = 0;
		int secondTryCorrect = 0;

		for (int i = 0; i < totalProblems; i++) {
			RandomProblem problem = new RandomProblem();
			System.out.println(problem.getProblem());

			double userAnswer = getUserAnswer(scanner);

			// Check the user's answer for the first time
			if (userAnswer == problem.getAnswer()) {
				System.out.println("Correct! Full credit.");
				totalScore++;
				firstTryCorrect++;
			} else {
				System.out.println("Incorrect. Try again.");

				userAnswer = getUserAnswer(scanner);

				// Check the user's answer second time
				if (userAnswer == problem.getAnswer()) {
					System.out.println("Correct on the second try. Half credit.");
					totalScore += 0.5;
					secondTryCorrect++;
				} else {
					System.out.println("Incorrect. The correct answer is: " + problem.getAnswer());
				}
			}
		}

		System.out.println("Quiz completed. Your score: " + totalScore + "/" + totalProblems);
		System.out.println("Correct on the first try: " + firstTryCorrect);
		System.out.println("Correct on the second try: " + secondTryCorrect);
		scanner.close();
	}

	private static double getUserAnswer(Scanner scanner) {
		System.out.print("Your answer: ");
		while (!scanner.hasNextDouble()) {
			System.out.println("Invalid input. Please enter a number.");
			scanner.next(); // consume the invalid input
			System.out.print("Your answer: ");
		}
		return scanner.nextDouble();
	}
}
