package com.lima.model;

public class VocabSudokuBoard {

    private static final int DEFAULT_DIMENSION = 9;
    private int dimension = DEFAULT_DIMENSION;

    private int[][] board;
    private WordPair[] wordList;

    public VocabSudokuBoard() {
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                board[i][j] = -1;
            }
        }
        wordList = new WordPair[dimension];
        wordList[0] = new WordPair("apple", "tao");
        wordList[1] = new WordPair("bee", "ong");
        wordList[2] = new WordPair("car", "xe");
        wordList[3] = new WordPair("decide", "quyet dinh");
        wordList[4] = new WordPair("extra", "them");
        wordList[5] = new WordPair("find", "tim");
        wordList[6] = new WordPair("guide", "huong dan");
        wordList[7] = new WordPair("home", "nha");
        wordList[8] = new WordPair("interesting", "thu vi");
    }

    public int getCell(int row, int col) {
        if (row >= dimension || col >= dimension)
            throw new IllegalArgumentException();
        return board[row][col];
    }

    public void setCell(int row, int col, int wordIndex) {
        if (row >= dimension || col >= dimension || wordIndex >= dimension)
            throw new IllegalArgumentException();
        board[row][col] = Math.max(wordIndex, -1);
        return;
    }

    public WordPair getWord(int wordIndex) {
        return wordList[wordIndex];
    }

    public int getDimension() {
        return dimension;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
