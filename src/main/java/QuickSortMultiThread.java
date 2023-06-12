import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class QuickSortMultiThread {

    private static class QuickSortThread extends RecursiveAction {
        private final int[] array;
        private final int low;
        private final int high;

        /**
         * Constructor for QuickSortThread
         * @param array the array to be sorted
         * @param low the lower bound of the array
         * @param high  the upper bound of the array
         */
        public QuickSortThread(int[] array, int low, int high){
            this.array = array;
            this.low = low;
            this.high = high;
        }

        public void timeTaken(){
            long startTime = System.nanoTime();
            QuickSortThread quickSortThread = new QuickSortThread(array, low, high);
            ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            forkJoinPool.invoke(quickSortThread);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in nanoseconds: " + timeElapsed);
        }

        /**
         * The main computation performed by this task.
         * This method first partitions the array
         * into two parts by calling partition method
         */
        @Override
        protected void compute() {
            if(low < high){
                int pi = partition(array, low, high);
                QuickSortThread left = new QuickSortThread(array, low, pi-1);
                QuickSortThread right = new QuickSortThread(array, pi+1, high);
                invokeAll(left, right);
            }
        }

        /**
         * This method is used to partition the given array and returns
         * the integer which points to the sorted pivot index
         * @param array the array to be sorted
         * @param low the lower bound of the array
         * @param max the upper bound of the array
         * @return the pivot index
         */

        private int partition(int[] array, int low, int max){
            int pivot = array[max];
            int pivotIndex = low ;
            for(int i = low; i < max; i++){
                if(array[i] <= pivot){
                    swap(array, i, pivotIndex);
                    pivotIndex++;
                }
            }
            swap(array, pivotIndex, max);
            return pivotIndex;
        }


        /**
         * This method is used to swap the values between the two given index
         * @param array the array to be sorted
         * @param i   the index to swap from
         * @param j   the index to swap to
         */

        private static void swap(int[] array, int i, int j){
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }




    }

    public static void main(String[] args){
        Random random = new Random();
        int[] array = new int [100];
        for(int i = 0; i < array.length -1; i++){
            array[i] = random.nextInt(1000);
        }
        System.out.println("Before sort: " + Arrays.toString(array));

        ForkJoinPool pool = new ForkJoinPool();
        QuickSortThread quickSortThread = new QuickSortThread(array, 0, array.length - 1);
        pool.invoke(quickSortThread);

        System.out.println("After sort: " + Arrays.toString(array));
        quickSortThread.timeTaken();

    }

}



