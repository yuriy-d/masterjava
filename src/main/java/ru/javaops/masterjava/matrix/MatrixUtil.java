package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final CompletionService<int[]> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < matrixSize ; i++) {
            int finalI = i;
            Future<int[]> submit = completionService.submit(new Callable<int[]>() {
                @Override
                public int[] call() throws Exception {
                    return multiplyRow(matrixA, matrixB, finalI);
                }
            });
            matrixC[i] = submit.get();
        }
        return matrixC;
    }

    public static int[] multiplyRow(int[][] matrixA, int[][] matrixB, int i){
        int[] matRow = new int [matrixA.length];
        for (int j = 0; j <matrixA.length ; j++) {
            matRow[j] = matrixB[j][i];
        }

        for (int j = 0; j < matrixA.length; j++) {
            int sum = 0;
            int[]row = matrixA[j];
            for (int k = 0; k < matrixA.length; k++) {
                sum += row[k] * matRow[k];
            }
            matRow[j] = sum;
        }
        return matRow;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        try {
            int[] matRow = new int [matrixSize];

            for (int i = 0;; i++){
                for (int j = 0; j <matrixSize ; j++) {
                    matRow[j] = matrixB[j][i];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    int[]row = matrixA[j];
                    for (int k = 0; k < matrixSize; k++) {
                        sum += row[k] * matRow[k];
                    }
                    matrixC[j][i] = sum;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return matrixC;
        }
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
