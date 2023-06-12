import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MergeSortMultiThread  {

    /**
     * This method is used to sort the elements on the left and right side of the pivot recursively
     * extend RecursiveAction
     */
    private static class MergeSortThread extends RecursiveAction {
        private final int[] array;
        private final int first;
        private final int last;

        /**
         * Constructor for MergeSortThread
         * @param array the array to be sorted
         * @param first the lower bound of the array
         * @param last the upper bound of the array
         */
        public MergeSortThread(int[] array, int first, int last) {
            this.array = array;
            this.first = first;
            this.last = last;
        }

        /**
         * This method is used to sort the elements on the left and right side of the pivot recursively
         * @param array the array to be sorted
         * @param first the lower bound of the array
         * @param last the upper bound of the array
         */
        public  void timeTake(int[] array, int first, int last){
            long startTime = System.nanoTime();
            MergeSortThread mergeSortThread = new MergeSortThread(array, first, last);
            ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            forkJoinPool.invoke(mergeSortThread);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in nanoseconds: " + timeElapsed);
        }


        /**
         * The main computation performed by this task.
         * This method first partitions the array
         */
        @Override
        protected void compute() {
            if (first < last) {
                int mid = (first + last) / 2;
                MergeSortThread left = new MergeSortThread(array, first, mid);
                MergeSortThread right = new MergeSortThread(array, mid + 1, last);
                invokeAll(left, right);
                merge(array, first, mid, last);
            }
        }


    }

    /**
     * This method is used to sort the elements on the left and right side of the pivot recursively
     * @param array the array to be sorted
     * @param first the lower bound of the array
     * @param mid the middle index of the array
     * @param high  the upper bound of the array
     */

    private static void merge(int[] array, int first, int mid, int high) {
        int n1 = mid - first + 1;
        int n2 = high - mid;

        int[] left = new int[n1];
        int[] right = new int[n2];

        System.arraycopy(array, first, left, 0, n1);
        for (int j = 0; j < n2; j++) {
            right[j] = array[mid + 1 + j];
        }
        int i = 0;
        int j = 0;
        int k = first;

        while (i < n1 && j < n2){
            if(left[i] <= right[j]){
                array[k] = left[i];
                i++;
            }else{
                array[k] = right[j];
                j++;
            }
            k++;
        }

        while(i < n1){
            array[k] = left[i];
            i++;
            k++;
        }
        while(j < n2){
            array[k] = right[j];
            j++;
            k++;
        }


    }




    public static void main(String[] args){
        Random random = new Random();
        int[] array = new int [100];
        for(int i = 0; i < array.length -1; i++){
            array[i] = random.nextInt(1000);
        }
        System.out.println("Before sort: " + Arrays.toString(array));
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        MergeSortThread mergeSortThread = new MergeSortThread(array, 0, array.length -1);
        forkJoinPool.invoke(mergeSortThread);
        System.out.println("After sort: " + Arrays.toString(array));
        mergeSortThread.timeTake(array, 0, array.length -1);

    }

}
