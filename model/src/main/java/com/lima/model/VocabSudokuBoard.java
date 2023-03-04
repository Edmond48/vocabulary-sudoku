package com.lima.model;

public class VocabSudokuBoard {

    // Public difficulty constants, represent the proportion of pre-filled cells
    public static final float DIFFICULTY_EASY = 0.45f;
    public static final float DIFFICULTY_MEDIUM = 0.30f;
    public static final float DIFFICULTY_HARD = 0.15f;

    // constant representing empty cell
    public static final int EMPTY_WORD = -1;

    // Default board dimension and sub-grid
    public static final int DEFAULT_DIMENSION = 9;
    private static final int DEFAULT_SUBGRID_HEIGHT = 3;
    private static final int DEFAULT_SUBGRID_WIDTH = 3;

    public static final int FOUR_DIMENSION = 4;
    private static final int FOUR_SUBGRID_HEIGHT = 2;
    private static final int FOUR_SUBGRID_WIDTH = 2;

    public static final int SIX_DIMENSION = 6;
    private static final int SIX_SUBGRID_HEIGHT = 2;
    private static final int SIX_SUBGRID_WIDTH = 3;

    public static final int TWELVE_DIMENSION = 12;
    private static final int TWELVE_SUBGRID_HEIGHT = 3;
    private static final int TWELVE_SUBGRID_WIDTH = 4;

    // TODO fix default value when menu is ready
    // private member variables for board dimension and subgrid
    private final int dimension;

    private final int gridWidth;
    private final int gridHeight;

    // board containing values 0 - (dimension - 1) which encodes the wordPairs used
    // special value: -1 means empty cell
    private final int[][] board;

    // keeps track of which cell is pre-filled, users cannot overwrite such cells
    private final boolean[][] isFixed;

    // list of wordPairs
    // use the numbers in board[][] correspond with the index
    private WordPair[] wordList;

    // TODO change to parameterized constructor for difficulty mode
    // default constructor
    // Creates a 9x9 board and fill 45% of the cells (easy mode)
    public VocabSudokuBoard(float difficulty, int dimension, String[] nativeWords, String[] foreignWords) {
        this.dimension = dimension;

        switch (this.dimension) {
            case FOUR_DIMENSION:
                gridHeight = FOUR_SUBGRID_HEIGHT;
                gridWidth = FOUR_SUBGRID_WIDTH;
                break;
            case SIX_DIMENSION:
                gridHeight = SIX_SUBGRID_HEIGHT;
                gridWidth = SIX_SUBGRID_WIDTH;
                break;
            case TWELVE_DIMENSION:
                gridHeight = TWELVE_SUBGRID_HEIGHT;
                gridWidth = TWELVE_SUBGRID_WIDTH;
                break;
            case DEFAULT_DIMENSION:
            default:
                gridHeight = DEFAULT_SUBGRID_HEIGHT;
                gridWidth = DEFAULT_SUBGRID_WIDTH;
                break;
        }

        this.board = new int[dimension][dimension];
        this.isFixed = new boolean[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                this.board[i][j] = EMPTY_WORD;
                this.isFixed[i][j] = false;
            }
        }

        fillWordList(nativeWords, foreignWords);

        generateBoard(difficulty);
    }

    // constructor build a board from a matrix of integers
    // assumes that the matrix is square
    VocabSudokuBoard(int[][] board) {
        this.dimension = board.length;
        this.gridWidth = dimension;
        this.gridHeight = dimension;
        this.board = new int[dimension][dimension];
        this.isFixed = new boolean[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                this.board[i][j] = board[i][j];
                this.isFixed[i][j] = (board[i][j] != EMPTY_WORD);
            }
        }

        // word list will have generic words
        fillWordList(null, null);
    }

    // get the integer representing the word pair at the given row and column index
    public int getCell(int row, int col) {
        if (row >= dimension || col >= dimension)
            throw new IllegalArgumentException();
        return board[row][col];
    }

    // set the integer representing the word pair at the given row and column index
    public void setCell(int row, int col, int wordIndex) {
        if (row >= dimension || col >= dimension || wordIndex >= dimension)
            throw new IllegalArgumentException();
        if (isFixed[row][col])
            return;
        board[row][col] = Math.max(wordIndex, EMPTY_WORD);
    }

    // get the word from the corresponding index
    public WordPair getWord(int wordIndex) {
        if (wordIndex == EMPTY_WORD)
            return new WordPair("", "");
        return wordList[wordIndex];
    }

    // getters
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

    // fill in the board and generate a valid puzzle
    // TODO fix a bug where a generated board is valid but not solvable
    private void generateBoard(float difficulty) {
        // generate a list of random coordinates
        int[] coordinates = new int[dimension * dimension];
        int targetNumFilledCells = (int) Math.floor(difficulty * dimension * dimension);

        for (int i = 0; i < coordinates.length; i++)
            coordinates[i] = i;

        shuffle(coordinates);

        // fill in cells randomly
        // TODO the bug is produced here when the cells are valid but from the user's perspective, there are no logical move
        for (int i = 0; i < targetNumFilledCells; i++) {
            int row = coordinates[i] / dimension;
            int col = coordinates[i] % dimension;

            int[] wordIndex = new int[dimension];
            for (int j = 0; j < wordIndex.length; j++) {
                wordIndex[j] = j;
            }
            shuffle(wordIndex);

            for (int index : wordIndex) {
                board[row][col] = index;
                if (validateCell(row, col)) {
                    isFixed[row][col] = true;
                    break;
                }
                board[row][col] = EMPTY_WORD;
            }
        }
    }

    // validate a cell by checking its row, column, and sub-grid for duplication
    private boolean validateCell(int row, int col) {
        int wordIndex = board[row][col];

        // if cell is empty, validation not needed
        if (wordIndex == EMPTY_WORD)
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
    // create a string from the current board
    // for testing
    public String toString() {
        StringBuilder boardStr = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardStr.append(String.format("%4s", board[i][j]));
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    // shuffle a given array
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

    // combine two string arrays into a wordPair array
    // and use it as this object's word pair
    private void fillWordList(String[] nativeWords, String[] foreignWords){
        wordList = new WordPair[dimension];

        if (nativeWords == null || foreignWords == null ||
                nativeWords.length != dimension || foreignWords.length != dimension) {
            for (int i = 0; i < dimension; i++) {
                wordList[i] = new WordPair("native" + i, "foreign" + i);
            }
            return;
        }

        for (int i = 0; i < dimension; i++)
            wordList[i] = new WordPair(nativeWords[i], foreignWords[i]);
    }

    // main function for testing
    public static void main(String[] args) {
        int[] myArr = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        shuffle(myArr);
        System.out.print("{");
        for (int i = 0; i < myArr.length - 1; i++)
            System.out.print(myArr[i] + ", ");
        System.out.println(myArr[myArr.length - 1] + "}");

        int[][] matrix = {
                {1, 2, 3, 4, 5, 6, 7, 8, 0},
                {-1, -1, -1, -1, -1, -1, -1, -1, 8},
                {2, 4, 5, -1, 1, 0, -1, 5, 0},
                {3, 8, 0, -1, -1, -1, -1, 3, -1},
                {3, 5, 7, -1, -1, 0, 7, 2, 1},
                {-1, -1, -1, -1, -1, -1, -1, -1, -1},
                {4, 5, 7, -1, 3, 0, 1, 2, 8},
                {-1 , -1, -1, 2, 4, 6, 4, 7, 0},
                {0, 1, 2, 3, 5, 6, 8, 7, -1},
                };
        VocabSudokuBoard myBoard = new VocabSudokuBoard(matrix);
        System.out.print(myBoard);
    }
}
