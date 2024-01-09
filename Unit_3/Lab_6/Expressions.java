import java.util.Random;

/**
 * A class for experimenting with expression trees.  This class includes 
 * a nested abstract class and several subclasses that represent nodes in
 * an expression tree.  It also includes several methods that work with these
 * classes.
 */
public class Expressions {
	
	/**
     * Computes the Root Mean Square Error (RMSE) for a given expression on sample data.
     *
     * @param f           The expression to evaluate.
     * @param sampleData  An array of input/output pairs for testing the expression.
     * @return The RMSE for the expression on the given sample data.
     */
    static double RMSError(ExpNode f, double[][] sampleData) {
        int n = sampleData.length;
        double sumSquaredErrors = 0;

        for (int i = 0; i < n; i++) {
            double x = sampleData[i][0];
            double y = sampleData[i][1];
            double predictedY = f.value(x);
            double error = y - predictedY;
            sumSquaredErrors += error * error;
        }

        double meanSquaredError = sumSquaredErrors / n;
        return Math.sqrt(meanSquaredError);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long duration = 10000; // 10 seconds

        ExpNode bestExpression = null;
        double bestRMSE = Double.MAX_VALUE;

        while (System.currentTimeMillis() - startTime < duration) {
            int randomHeight = new Random().nextInt(4);
            ExpNode randomExp = randomExpression(randomHeight);
            

            // Use makeTestData to generate sample data for testing the expression
            double[][] testData = makeTestData();
            double currentRMSE = RMSError(randomExp, testData);

            if (currentRMSE < bestRMSE) {
                bestRMSE = currentRMSE;
                bestExpression = randomExp;
            }
        }

        System.out.println("Testing expression creation and evaluation...\n");

        ExpNode e1 = new BinOpNode('+', new VariableNode(), new ConstNode(3));

        ExpNode e2 = new BinOpNode('*', new ConstNode(2), new VariableNode());

        ExpNode e3 = new BinOpNode('-', e1, e2);

        ExpNode e4 = new BinOpNode('/', e1, new ConstNode(-3));

        System.out.println("For x = 3:");

        System.out.println(" " + e1 + " = " + e1.value(3));

        System.out.println(" " + e2 + " = " + e2.value(3));

        System.out.println(" " + e3 + " = " + e3.value(3));

        System.out.println(" " + e4 + " = " + e4.value(3));

        System.out.println("\nTesting copying...");

        System.out.println("Best Expression found:");
        System.out.println(bestExpression);
        System.out.println("RMSE: " + bestRMSE);
    }
	
    static ExpNode randomExpression(int maxHeight) {
        Random rand = new Random();

        if (maxHeight == 0) {
            // If maxHeight is zero, choose between ConstantNode, VariableNode, and UnaryOpNode
            int randomChoice = rand.nextInt(3);
            if (randomChoice == 0) {
            	return new ConstNode(rand.nextDouble() * 20 - 10);
            } else if (randomChoice == 1) {
                return new VariableNode();
            } else {
                char unaryOperator = getRandomUnaryOperator(rand);
                ExpNode operand = randomExpression(maxHeight - 1);
                return new UnaryOpNode(unaryOperator, operand);
            }
        } else {
            // Randomly choose between ConstantNode, VariableNode, UnaryOpNode, and BinOpNode
            int randomChoice = rand.nextInt(4);
            if (randomChoice == 0) {
            	return new ConstNode(rand.nextDouble() * 20 - 10);
            } else if (randomChoice == 1) {
                return new VariableNode(); 
            } else if (randomChoice == 2) {
                char unaryOperator = getRandomUnaryOperator(rand);
                ExpNode operand = randomExpression(maxHeight - 1);
                return new UnaryOpNode(unaryOperator, operand);
            } else {
                char binaryOperator = getRandomBinaryOperator(rand);
                ExpNode left = randomExpression(maxHeight - 1);
                ExpNode right = randomExpression(maxHeight - 1);
                return new BinOpNode(binaryOperator, left, right);
            }
        }
    }

    private static char getRandomUnaryOperator(Random rand) {
        char[] operators = {'s', 'c', 'e', 'a'};
        return operators[rand.nextInt(operators.length)];
    }
    
    // Helper method to get a random binary operator
    private static char getRandomBinaryOperator(Random rand) {
        char[] operators = {'+', '-', '*', '/'};
        return operators[rand.nextInt(operators.length)];
    }
	
	/**
	 * Given an ExpNode that is the root of an expression tree, this method
	 * makes a full copy of the tree.  The tree that is returned is constructed
	 * entirely of freshly made nodes.  (That is, there are no pointers back
	 * into the old copy.)
	 */
	static ExpNode copy(ExpNode root) {
		if (root instanceof ConstNode)
			return new ConstNode(((ConstNode)root).number);
		else if (root instanceof VariableNode)
			return new VariableNode();
		else {
			BinOpNode node = (BinOpNode)root;
			// Note that left and right operand trees have to be COPIED, 
			// not just referenced.
			return new BinOpNode(node.op, copy(node.left), copy(node.right));
		}
	}
	
	/**
	 * Returns an n-by-2 array containing sample input/output pairs. If the
	 * return value is called data, then data[i][0] is the i-th input (or x)
	 * value and data[i][1] is the corresponding output (or y) value.
	 * (This method is currently unused, except to test it.)
	 */
	static double[][] makeTestData() {
		double[][] data = new double[21][2];
		double xmax = 5;
		double xmin = -5;
		double dx = (xmax - xmin) / (data.length - 1);
		for (int i = 0; i < data.length; i++) {
			double x = xmin + dx * i;
			double y = 2.5*x*x*x - x*x/3 + 3*x;
			data[i][0] = x;
			data[i][1] = y;
		}
		return data;
	}
	
	
	//------------------- Definitions of Expression node classes ---------
	
	/**
	 * An abstract class that represents a general node in an expression
	 * tree.  Every such node must be able to compute its own value at
	 * a given input value, x.  Also, nodes should override the standard
	 * toString() method to return a fully parameterized string representation
	 * of the expression.
	 */
	static abstract class ExpNode {
		abstract double value(double x);
		// toString method should also be defined in each subclass
	}
	
	/**
	 * A node in an expression tree that represents a constant numerical value.
	 */
	static class ConstNode extends ExpNode {
		double number;  // the number in this node.
		ConstNode(double number) {
			this.number = number;
		}
		double value(double x) {
			return number;
		}
		public String toString() {
			if (number < 0)
				return "(" + number + ")"; // add parentheses around negative number
			else
				return "" + number;  // just convert the number to a string
		}
	}
	
	/**
	 * A node in an expression tree that represents the variable x.
	 */
	static class VariableNode extends ExpNode {
		VariableNode() {
		}
		double value(double x) {
			return x;
		}
		public String toString() {
			return "x";
		}
	}
	
	/**
	 * A node in an expression tree that represents one of the
	 * binary operators +, -, *, or /.
	 */
	static class BinOpNode extends ExpNode {
		char op;  // the operator, which must be '+', '-', '*', or '/'
		ExpNode left, right;  // the expression trees for the left and right operands.
		BinOpNode(char op, ExpNode left, ExpNode right) {
			if (op != '+' && op != '-' && op != '*' && op != '/')
				throw new IllegalArgumentException("'" + op + "' is not a legal operator.");
			this.op = op;
			this.left = left;
			this.right = right;
		}
		double value(double x) {
			double a = left.value(x);  // value of the left operand expression tree
			double b = right.value(x); // value of the right operand expression tree
			switch (op) {
			case '+': return a + b;
			case '-': return a - b;
			case '*': return a * b;
			default:  return a / b;
			}
		}
		public String toString() {
			return "(" + left.toString() + op + right.toString() + ")";
		}
	}

	/**
     * A node in an expression tree that represents a unary mathematical function.
     */
    static class UnaryOpNode extends ExpNode {
        char op;  // the operator, which must be 's' (sin), 'c' (cos), 'e' (exp), or 'a' (abs)
        ExpNode operand;  // the expression tree for the operand.

        UnaryOpNode(char op, ExpNode operand) {
            if (op != 's' && op != 'c' && op != 'e' && op != 'a') {
                throw new IllegalArgumentException("'" + op + "' is not a legal unary operator.");
            }
            this.op = op;
            this.operand = operand;
        }

        double value(double x) {
            double a = operand.value(x);  // value of the operand expression tree
            switch (op) {
                case 's':
                    return Math.sin(a);
                case 'c':
                    return Math.cos(a);
                case 'e':
                    return Math.exp(a);
                case 'a':
                    return Math.abs(a);
                default:
                    throw new IllegalArgumentException("Unexpected unary operator: " + op);
            }
        }

        public String toString() {
            return op + "(" + operand.toString() + ")";
        }
    }
}