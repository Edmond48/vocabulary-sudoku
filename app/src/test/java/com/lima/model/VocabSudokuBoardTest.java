package com.lima.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VocabSudokuBoardTest {

    //Create object using matrix
    private final int [][] arr_2d = {
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

    private final String[] nativeWords =
            {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private final String[] foreignWords =
            {"oneF", "twoF", "threeF", "fourF", "fiveF", "sixF", "sevenF", "eightF", "nineF"};


    private final VocabSudokuBoard array_vsb = new VocabSudokuBoard(arr_2d);
    private final VocabSudokuBoard nine_vsb = new VocabSudokuBoard(
            VocabSudokuBoard.DIFFICULTY_EASY,
            VocabSudokuBoard.DEFAULT_DIMENSION,
            nativeWords,
            foreignWords
    );

    private final VocabSudokuBoard four_vsb = new VocabSudokuBoard(
            VocabSudokuBoard.DIFFICULTY_EASY,
            VocabSudokuBoard.FOUR_DIMENSION,
            nativeWords,
            foreignWords
    );

    private final VocabSudokuBoard six_vsb = new VocabSudokuBoard(
            VocabSudokuBoard.DIFFICULTY_EASY,
            VocabSudokuBoard.SIX_DIMENSION,
            nativeWords,
            foreignWords
    );

    private final VocabSudokuBoard twelve_vsb = new VocabSudokuBoard(
            VocabSudokuBoard.DIFFICULTY_EASY,
            VocabSudokuBoard.TWELVE_DIMENSION,
            nativeWords,
            foreignWords
    );


    // Checks if the function returns the expected element of matrix
    @Test
    public void getCell() {
        for (int i = 0; i < array_vsb.getDimension(); i++) {
            for (int j = 0; j < array_vsb.getDimension(); j++) {
                assertEquals(arr_2d[i][j], array_vsb.getCell(i, j));
            }
        }
    }

    // Tests exception throwing for a cell
    @Test(expected = IllegalArgumentException.class)
    public void getCellThrows() {
        array_vsb.getCell(10,1);
        four_vsb.getCell(4, 4);
        six_vsb.getCell(6, 6);
        nine_vsb.getCell(9, 5);
        twelve_vsb.getCell(11, 12);
    }

    // Tests if wordIndex is assigned to the cell
    @Test
    public void setCell() {
        int temp;

        array_vsb.setCell(1,0,5);
        assertEquals(array_vsb.getCell(1, 0), 5);

        temp = four_vsb.isFixed(2, 3) ? four_vsb.getCell(2, 3) : 0;
        four_vsb.setCell(2, 3, temp);
        assertEquals(four_vsb.getCell(2, 3), temp);

        temp = six_vsb.isFixed(4, 5) ? six_vsb.getCell(4, 5) : 1;
        six_vsb.setCell(4, 5, temp);
        assertEquals(six_vsb.getCell(4, 5), temp);

        temp = nine_vsb.isFixed(6, 7) ? nine_vsb.getCell(6, 7) : 3;
        nine_vsb.setCell(6, 7, temp);
        assertEquals(nine_vsb.getCell(6, 7), temp);

        temp = twelve_vsb.isFixed(8, 9) ? twelve_vsb.getCell(8, 9) : -1;
        twelve_vsb.setCell(8, 9, temp);
        assertEquals(twelve_vsb.getCell(8, 9), temp);
    }

    // Tests exception throwing for setting a cell
    @Test(expected = IllegalArgumentException.class)
    public void setCellThrows() {
        array_vsb.setCell(1,1,10);
        four_vsb.setCell(4, 5, 2);
        six_vsb.setCell(2, 3, 7);
        nine_vsb.setCell(10, 2, 10);
        twelve_vsb.setCell(4, 9, 12);
    }

    //Tests if the function returns the expected word pair
    @Test
    public void getWord() {
        WordPair tempPair = array_vsb.getWord(4);
        assertEquals("native4", tempPair.getNativeWord());
        assertEquals("foreign4", tempPair.getForeignWord());

        tempPair = four_vsb.getWord(0);
        assertEquals("native0", tempPair.getNativeWord());
        assertEquals("foreign0", tempPair.getForeignWord());

        tempPair = six_vsb.getWord(3);
        assertEquals("native3", tempPair.getNativeWord());
        assertEquals("foreign3", tempPair.getForeignWord());

        tempPair = nine_vsb.getWord(8);
        assertEquals(nativeWords[8], tempPair.getNativeWord());
        assertEquals(foreignWords[8], tempPair.getForeignWord());

        tempPair = twelve_vsb.getWord(11);
        assertEquals("native11", tempPair.getNativeWord());
        assertEquals("foreign11", tempPair.getForeignWord());
    }

    // Tests the function when cell is empty
    @Test
    public void getWordForEmptyCell() {
        WordPair tempPair = array_vsb.getWord(-1);
        assertEquals("", tempPair.getNativeWord());
        assertEquals("", tempPair.getForeignWord());

        tempPair = four_vsb.getWord(-1);
        assertEquals("", tempPair.getNativeWord());
        assertEquals("", tempPair.getForeignWord());

        tempPair = six_vsb.getWord(-1);
        assertEquals("", tempPair.getNativeWord());
        assertEquals("", tempPair.getForeignWord());

        tempPair = nine_vsb.getWord(-1);
        assertEquals("", tempPair.getNativeWord());
        assertEquals("", tempPair.getForeignWord());

        tempPair = twelve_vsb.getWord(-1);
        assertEquals("", tempPair.getNativeWord());
        assertEquals("", tempPair.getForeignWord());
    }
}