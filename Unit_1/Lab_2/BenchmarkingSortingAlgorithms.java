import java.util.Arrays;

/**
 * Time Records
 * When the array size = 1,000,
 * Time taken to sort using Selection Sort: 0.003 seconds
 * Time taken to sort using Arrays.sort(): 0.001 seconds
 * 
 * When the array size = 10,000,
 * Time taken to sort using Selection Sort: 0.053 seconds
 * Time taken to sort using Arrays.sort(): 0.005 seconds
 * 
 * When the array size = 100,000,
 * Time taken to sort using Selection Sort: 3.912 seconds
 * Time taken to sort using Arrays.sort(): 0.034 seconds
 * 
 * When the array size = 1,000,000,
 * Time taken to sort using Arrays.sort(): 0.409 seconds
 */
public class BenchmarkingSortingAlgorithms {

	// Constant for array size
	private static final int SIZE = 1000;

	public static void main(String[] args) {
		// Create two arrays with the same size
		int[] array1 = new int[SIZE];
		int[] array2 = new int[SIZE];

		// Fill the arrays with random integers
		fillArrayWithRandomIntegers(array1);
		fillArrayWithRandomIntegers(array2);
		
		// Time the selection sort
        long startTime = System.currentTimeMillis();
        // Sort the first array using Selection Sort
        selectionSort(array1);
        long endTime = System.currentTimeMillis();
        
        // The time taken in seconds
        long selectionSortTime = endTime - startTime;
        System.out.println("Time taken to sort using Selection Sort: " + selectionSortTime / 1000.0 + " seconds");
        
        // Time the Arrays.sort()
        long startTime2 = System.currentTimeMillis();
        // Sort the first array using Selection Sort
        Arrays.sort(array2);
        long endTime2 = System.currentTimeMillis();
        
     // The time taken in seconds
        long arraysSortTime = endTime2 - startTime2;
        System.out.println("Time taken to sort using Arrays.sort(): " + arraysSortTime / 1000.0 + " seconds");
	}

	// Method to fill an array with random integers
	private static void fillArrayWithRandomIntegers(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = (int) (Integer.MAX_VALUE * Math.random());
		}
	}
	
	// The Selection Sort method copy from Section 7.4
	private static void selectionSort(int[] A) {
		// Sort A into increasing order, using selection sort
		for (int lastPlace = A.length-1; lastPlace > 0; lastPlace--) {
			// Find the largest item among A[0], A[1], ...,
			// A[lastPlace], and move it into position lastPlace
			// by swapping it with the number that is currently
			// in position lastPlace.
			int maxLoc = 0; // Location of largest item seen so far.
			for (int j = 1; j <= lastPlace; j++) {
				if (A[j] > A[maxLoc]) {
					// Since A[j] is bigger than the maximum we’ve seen
					// so far, j is the new location of the maximum value
					// we’ve seen so far.
					maxLoc = j;
				}
			}
			int temp = A[maxLoc]; // Swap largest item with A[lastPlace].
			A[maxLoc] = A[lastPlace];
			A[lastPlace] = temp;
		} // end of for loop
	}
}
