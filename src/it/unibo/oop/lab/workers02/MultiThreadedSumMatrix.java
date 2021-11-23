package it.unibo.oop.lab.workers02;


import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 *
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThread;

    /**
     * @param nThread number of thread worker
     */
    public MultiThreadedSumMatrix(final int nThread) {
        this.nThread = nThread;
    }

    private static class Worker extends Thread {
        private final int start;
        private final int nElm;
        private final double[][] matrix;
        private double res;

        Worker(final double[][] matrix, final int start,  final int nElm) {
            super();
            this.start = start;
            this.nElm = nElm;
            this.matrix = matrix.clone();
        }

        @Override
        public  void run() {
            int count = 0;
            final int startX = this.start / this.matrix[0].length;
            int y = this.start % this.matrix[0].length;
            int x;
            System.out.println("Working from position " + startX + "," + y + " for  " + nElm);
            for (x = startX; x < this.matrix[0].length && count < nElm; x++) {
                for (; y < this.matrix[0].length && count < nElm; y++) {
                    this.res += this.matrix[x][y];
                    count++;
                }
                y = 0;
            }
        }

        public double getResult() {
            return this.res;
        }

    }

    @Override
    public double sum(final double[][] matrix) {
        final int sizeM = matrix.length * matrix.length;
        final int size = sizeM / this.nThread + sizeM % this.nThread;

        final List<Worker> workers = new ArrayList<>();

        for (int start = 0; start < sizeM; start += size) {
            workers.add(new Worker(matrix, start, size));

        }


        for (final var w: workers) {
            w.start();
        }

        double sum = 0;

        for (final var w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        /*
         * Return the sum
         */
        return sum;

    }


}
