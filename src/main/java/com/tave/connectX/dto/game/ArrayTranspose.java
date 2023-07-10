package com.tave.connectX.dto.game;

public abstract class ArrayTranspose {

    // server to client
    public static int[][] ArrayTranspose(int[][] arr) {

        int[][] transpose = new int[arr[0].length][arr.length];

        for (int i = 0; i < transpose.length; i++) {
            for (int j = 0; j < transpose[0].length; j++) {
                transpose[i][j] = arr[arr.length-1 - j][i];
            }
        }
        return transpose;
    }

    // client to server
    public static int[][] ArrayTransposeReverse(int[][] arr) {

        int[][] transpose = new int[arr[0].length][arr.length];

        for (int i = 0; i < transpose.length; i++) {
            for (int j = 0; j < transpose[0].length; j++) {
                transpose[i][j] = arr[j][arr[0].length - 1-i];
            }
        }
        return transpose;
    }

}
