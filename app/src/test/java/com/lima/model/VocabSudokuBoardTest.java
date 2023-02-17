package com.lima.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class VocabSudokuBoardTest {

    //Create object using matrix
    int [][] arr_2d = {
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
    private VocabSudokuBoard vsb = new VocabSudokuBoard(arr_2d);


    // Checks if the function returns the expected element of matrix
    @Test
    public void getCell() {
        assertEquals(4, vsb.getCell(2, 1));
    }

    // Tests exception throwing for a cell
    @Test(expected = IllegalArgumentException.class)
    public void getCellThrows() {
        vsb.getCell(10,1);
    }

    // Tests if wordIndex is assigned to the cell
    @Test
    public void setCell() {
        vsb.setCell(1,0,5);
    }

    // Tests exception throwing for setting a cell
    @Test(expected = IllegalArgumentException.class)
    public void setCellThrows() {
        vsb.setCell(1,1,10);
    }

    //Tests if the function returns the expected word pair
    @Test
    public void getWord() {
        WordPair vsbWord = vsb.getWord(4);
        assertEquals("extra", vsbWord.getNativeWord());
        assertEquals("them", vsbWord.getForeignWord());
    }

    // Tests the function when cell is empty
    @Test
    public void getWordForEmptyCell() {
        WordPair vsbWord = vsb.getWord(-1);
        assertEquals("", vsbWord.getNativeWord());
        assertEquals("", vsbWord.getForeignWord());
    }
}