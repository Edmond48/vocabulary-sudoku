package com.lima.model;

public class VocabSudokuBoard {

    public static final float DIFFICULTY_EASY = 0.45f;
    public static final float DIFFICULTY_MEDIUM = 0.30f;
    public static final float DIFFICULTY_HARD = 0.15f;
    public static final int EMPTY_WORD = -1;

    private static final int DEFAULT_DIMENSION = 9;
    private static final int DEFAULT_SUBGRID_HEIGHT = 3;
    private static final int DEFAULT_SUBGRID_WIDTH = 3;

    private final int dimension = DEFAULT_DIMENSION;

    private final int gridWidth = DEFAULT_SUBGRID_WIDTH;
    private final int gridHeight = DEFAULT_SUBGRID_HEIGHT;

    private int[][] board;
    private boolean[][] isFixed;
    private WordPair[] wordList;

    public VocabSudokuBoard() {
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                board[i][j] = -1;
            }
        }

        isFixed = new boolean[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                isFixed[i][j] = false;
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

        generateBoard(DIFFICULTY_EASY);
    }

    public int getCell(int row, int col) {
        if (row >= dimension || col >= dimension)
            throw new IllegalArgumentException();
        return board[row][col];
    }

    public void setCell(int row, int col, int wordIndex) {
        if (isFixed[row][col])
            return;
        if (row >= dimension || col >= dimension || wordIndex >= dimension)
            throw new IllegalArgumentException();
        board[row][col] = Math.max(wordIndex, -1);
    }

    public WordPair getWord(int wordIndex) {
        return wordList[wordIndex];
    }

    public int getDimension() {
        return dimension;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public boolean isFixed(int row, int col) {
        return isFixed[row][col];
    }

    private void generateBoard(float difficulty) {
        int[] coordinates = new int[dimension * dimension];
        int targetNumFilledCells = (int) Math.floor(difficulty * dimension * dimension);

        for (int i = 0; i < coordinates.length; i++)
            coordinates[i] = i;

        // shuffle
        shuffle(coordinates);

        // fill in cells randomly
        for (int i = 0; i < targetNumFilledCells; i++) {
            int row = coordinates[i] / dimension;
            int col = coordinates[i] % dimension;

            int[] wordIndex = new int[dimension];
            for (int j = 0; j < wordIndex.length; j++) {
                wordIndex[j] = j;
            }
            shuffle(wordIndex);

            for (int j = 0; j < wordIndex.length; j++) {
                board[row][col] = wordIndex[j];
                if (validateCell(row, col)) {
                    isFixed[row][col] = true;
                    break;
                }
                board[row][col] = -1;
            }
        }
    }

    private boolean validateCell(int row, int col) {
        int wordIndex = board[row][col];

        // if cell is empty, validation not needed
        if (wordIndex == -1)
            return true;

        // validate row
        for (int i = 0; i < dimension; i++) {
            if (board[row][i] == wordIndex && i != col)
                return false;
        }

        // validate column
        for (int i = 0; i < dimension; i++) {
            if (board[i][col] == wordIndex && i != row)
                return false;
        }

        // validate subgrid
        // find the coordinate of the top left cell of the sub-grid
        int rowStart = row / gridHeight * gridHeight;
        int colStart = col / gridWidth * gridWidth;

        // loop through coordinates of the sub-grid
        for (int i = rowStart; i < rowStart + gridHeight; i++) {
            for (int j = colStart; j < colStart + gridWidth; j++){
                if (board[i][j] == wordIndex && i != row && j != col)
                    return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardStr.append(board[i][j]).append("\t");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    private static void shuffle(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            // generate index for element at i between i and arr.length - 1
            int index = i + (int) (Math.random() * Integer.MAX_VALUE) % (arr.length - i);

            // swap elements at index and i
            int temp = arr[i];
            arr[i] = arr[index];
            arr[index] = temp;
        }
    }

    public static void main(String[] args) {
        int[] myArr = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        shuffle(myArr);
        System.out.print("{");
        for (int i = 0; i < myArr.length - 1; i++)
            System.out.print(myArr[i] + ", ");
        System.out.println(myArr[myArr.length - 1] + "}");

        VocabSudokuBoard myBoard = new VocabSudokuBoard();
        System.out.print(myBoard);
    }
}
