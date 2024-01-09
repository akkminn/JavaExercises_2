import java.util.TreeSet;
import textio.TextIO;

/**
 * A simple program that can compute set Operation: Union
 * Intersection, Difference. The program 
 */
public class SetCalculator {

	public static void main (String[] args) throws ParseError {

		TreeSet<Integer> setA, setB; // Two operand sets A and B
		char op; // Operator

		System.out.println("This is a set calculator which can ");
		System.out.println("perform Union, Intersection, and Difference.");
		System.out.println("");
		System.out.println("Use '+' for Union, '*' for Intersection, ");
		System.out.println("'-' for Difference!");
		System.out.println("Enter set problem (press enter to end):");

		while (true) {
			if (TextIO.peek() == '\n') {
	               // The input line is empty.  
	               // Exit the loop and end the program.
				System.out.print("Thanks for using our program!");
	            break;
	         }
			try {
				setA = getSet(); // Get setA

				TextIO.skipBlanks();
				op = getOperator();

				TextIO.skipBlanks();
				setB = getSet(); // Get setB

				// Perform set Calculation
				setCalculation(setA, setB, op);
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			TextIO.getln();
		}
		
	}

	/**
	 * Get the operator from the user input. '+' for Union, 
	 * '*' for Intersection, '-' for Difference!
	 * 
	 * @return
	 * @throws ParseError
	 */
	private static char getOperator() throws ParseError {

		char op = TextIO.peek(); // Look the operator
		if (op != '+' && op != '*' && op != '-') 
			throw new ParseError("Unexpected Operator!!");
		op = TextIO.getAnyChar(); // Read the operator.
		return op;
	}

	/**
	 * Get a set of none-negative integer, from the user
	 * as an input. The set must be enclosed between square 
	 * brackets and must contain a list of zero or
	 * more non-negative integers, separated by commas.
	 * All the blanks are skipped using TextIO.skipBlanks().
	 * If an error occurs, throws a ParseError with an error 
	 * message.
	 * 
	 * @return set type of TreeSet<Integer>
	 * @throws ParseError
	 */
	private static TreeSet<Integer> getSet() throws ParseError {

		// Create a new TreeSet
		TreeSet<Integer> set = new TreeSet<>();

		// Skip the blanks with TextIO.skipblanks()
		TextIO.skipBlanks();
		if (TextIO.peek() != '[')
			throw new ParseError("Expected '[' at start of set.");
		TextIO.getAnyChar(); // Read the '['.

		// Skip the blanks
		TextIO.skipBlanks();
		if (TextIO.peek() == ']') {
			// The set has no integers.  This is the empty set, which
			// is legal.  Return the value.
			TextIO.getAnyChar(); // Read the ']'.
			return set;
		}

		while (true) {
			// Read the next integer and add it to the set.
			TextIO.skipBlanks(); // Skip the blanks
			if (! Character.isDigit(TextIO.peek()))
				throw new ParseError("Expected an integer.");
			int n = TextIO.getInt(); // Read the integer.
			set.add( Integer.valueOf(n) );  // (Could have just said set.add(n)!)
			TextIO.skipBlanks();
			if (TextIO.peek() == ']')
				break;  // ']' marks the end of the set.
			else if (TextIO.peek() == ',')
				TextIO.getAnyChar(); // Read a comma and continue.
			else
				throw new ParseError("Expected ',' or ']'.");
		}

		TextIO.getAnyChar(); // Read the ']' that ended the set.

		return set;
	}

	/**
	 * An object of type ParseError represents a syntax error found in 
	 * the user's input. 
	 */
	private static class ParseError extends Exception {
		ParseError(String message) {
			super(message);
		}
	} // end nested class ParseError


	/**
	 * Perform set Calculation (Union, Intersection, Difference)
	 * based on two given sets and a operator. If an error occurs, 
	 * throws a ParseError.
	 * 
	 * @param setA TreeSet of type Integer as input from user
	 * @param setB TreeSet of type Integer as input from user
	 * @param op An operator to perform set calculation
	 * @return A TreeSet of type Integer
	 * @throws ParseError
	 */
	public static void setCalculation(TreeSet<Integer> setA, 
			TreeSet<Integer> setB, char op) throws ParseError {

		switch(op) {
		case '+': // If the operator is '+', perform union
			setA.addAll(setB);
			break; 
		case '*': // If the operator is '*', perform intersection
			setA.retainAll(setB);
			break;
		case '-': // If the operator is '/', perform difference
			setA.removeAll(setB);
			break;
		default: // If the operator is others type, throw error message
			throw new ParseError("Unexpected Operator!!");
		}

		System.out.println("Output");
		System.out.println(setA);
	}

}
