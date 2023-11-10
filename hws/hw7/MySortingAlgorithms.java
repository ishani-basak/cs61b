import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int i = 1;
            while (i < k && i < array.length) {
                int j = i;
                while (j > 0) {
                    if (array[j] >= array[j-1]) {
                        break;
                    } else {
                        swap(array, j, j-1);
                        j--;
                    }
                }
                i++;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int i = 0;
            while (i < k && i < array.length) {
                int min = array[i];
                int minI = i;
                for (int m = i+1; m < k; m++) {
                    if (array[m] < min) {
                        min = array[m];
                        minI = m;
                    }
                }
                if (minI != i) {
                    swap(array, i, minI);
                }
                i++;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k > 1) {
                int[] arr1 = new int[k/2];
                int[] arr2 = new int[k - k/2];
                System.arraycopy(array, 0, arr1, 0, arr1.length);
                System.arraycopy(array, k/2, arr2, 0, arr2.length);
                int[] result = merge(arr1, arr2);
                for (int i = 0; i < k; i++) {
                    array[i] = result[i];
                }
            }
        }

        public int[] merge(int[] arr1, int[] arr2) {
            if (arr1.length > 1) {
                sort(arr1, arr1.length);
            }
            if (arr2.length > 1) {
                sort(arr2, arr2.length);
            }
            int[] result = new int[arr1.length + arr2.length];
            int i = 0;
            int a = 0;
            int b = 0;
            while (i < result.length) {
                if (a < arr1.length && b < arr2.length) {
                    if (arr1[a] <= arr2[b]) {
                        result[i] = arr1[a];
                        a++;
                    } else {
                        result[i] = arr2[b];
                        b++;
                    }
                } else if (a < arr1.length) {
                    result[i] = arr1[a];
                    a++;
                } else {
                    result[i] = arr2[b];
                    b++;
                }

                i++;
            }
            return result;
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
            int max = 0;
            for (int i : a) {
                max = Math.max(max, i);
            }
            int d = 1;
            while (max / d > 0) {
                int i = 1;
                while (i < k && i < a.length) {
                    int j = i;
                    while (j > 0) {
                        if (a[j] / d % 10 >= a[j-1] / d % 10) {
                            break;
                        } else {
                            swap(a, j, j-1);
                            j--;
                        }
                    }
                    i++;
                }
                d *= 10;
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
