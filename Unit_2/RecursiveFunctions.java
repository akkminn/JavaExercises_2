public class RecursiveFunctions {

    // Recursive function to compute factorial
    public static long factorial(int N) {
        if (N == 0 || N == 1) {
            return 1;
        } else {
            return N * factorial(N - 1);
        }
    }

    // Recursive function to compute fibonacci
    public static long fibonacci(int N) {
        if (N == 0) {
            return 0;
        } else if (N == 1 || N == 2) {
            return 1;
        } else {
            return fibonacci(N - 1) + fibonacci(N - 2);
        }
    }

    // Main routine to test the recursive functions
    public static void main(String[] args) {
        // Test factorial function
        int factorialN = 5;
        long resultFactorial = factorial(factorialN);
        System.out.println("Factorial of " + factorialN + " is: " + resultFactorial);

        // Test fibonacci function
        int fibonacciN = 8;
        long resultFibonacci = fibonacci(fibonacciN);
        System.out.println("Fibonacci of " + fibonacciN + " is: " + resultFibonacci);
    }
}
