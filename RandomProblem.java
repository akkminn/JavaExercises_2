import java.util.Random;

/**
 * A simple program to generate a random math quiz question.
 */
public class RandomProblem {

	private int x,y;
	private double answer;
	private char[] operators = {'+', '-', '*', '/'};
	private char randomOperator;
	
	// Create a random object
	Random random = new Random();
	
	/**
     * Constructor to initialize the random math problem.
     */
    public RandomProblem() {
    	x = random.nextInt(30);
        y = random.nextInt(30) + 1; // Make sure divisor is not zero
        randomOperator = operators[random.nextInt(operators.length)];
        answer = calculateAnswer(x, y, randomOperator);
    }
    

    private static double calculateAnswer(int x, int y, char randomOperator) {
    	switch (randomOperator) {
    	case '+':
    		return x + y;
    	case '-':
    		return x - y;
    	case '*':
    		return x * y;
    	case '/':
    		double result = (double) x / y;
            return Math.round(result * 100.0) / 100.0;
    	default:
    		throw new IllegalArgumentException("Invalid operator: " + randomOperator);
    	}
    }
    
    /**
    * Gets the formatted math problem as a string.
    *
    * @return The math problem.
    */
	public String getProblem() {
    	return "Compute the sum: " + x + randomOperator + y;
    }

	/**
     * Gets the correct answer to the math problem.
     *
     * @return The correct answer.
     */
    public double getAnswer() {
    	return answer;
    }
}
