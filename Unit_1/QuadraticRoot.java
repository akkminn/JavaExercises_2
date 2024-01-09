import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A simple program to calculate the root of a quadratic equation.
 */
public class QuadraticRoot {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		// Repeat until the user wants to stop
		while (true) {
			try {
				// Ask the user to specify values for A, B, and C
				System.out.print("Enter the value for A: ");
				double A = scanner.nextDouble();

				System.out.print("Enter the value for B: ");
				double B = scanner.nextDouble();

				System.out.print("Enter the value for C: ");
				double C = scanner.nextDouble();

				// Calculate and print the root
				double root = root(A, B, C);
				System.out.println("The root is: " + root);

				// Ask if the user wants to enter another equation
				System.out.print("Do you want to enter another equation? (yes/no): ");
				String answer = scanner.next().toLowerCase();

				if (answer.equals("no")) {
					System.out.println("Goodbye!");
					break;  // Exit the loop if the user doesn't want to continue
				}
			} catch (InputMismatchException e) {
                // Catch and print InputMismatchException
                System.out.println("Error: Please enter valid numerical values.");
                scanner.nextLine();
			} catch (IllegalArgumentException e) {
				// Catch and print any exceptions
				System.out.println("Error: " + e.getMessage());
				scanner.nextLine(); 
			}
		}

		// Close the scanner
		scanner.close();
	}

	/**
	 * Returns the larger of the two roots of the quadratic equation
	 * A*x*x + B*x + C = 0, provided it has any roots.  If A == 0 or
	 * if the discriminant, B*B - 4*A*C, is negative, then an exception
	 * of type IllegalArgumentException is thrown.
	 */
	public static double root( double A, double B, double C )
			throws IllegalArgumentException {
		if (A == 0) {
			throw new IllegalArgumentException("A can't be zero.");
		}
		else {
			double disc = B*B - 4*A*C;
			if (disc < 0)
				throw new IllegalArgumentException("Discriminant < zero.");
			return  (-B + Math.sqrt(disc)) / (2*A);
		}
	}
}
