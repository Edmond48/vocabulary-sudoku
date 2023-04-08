package com.lima.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WordPairTest {

    private final int N = 9;
    private final String[] nativeWords =
            {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private final String[] foreignWords =
            {"oneF", "twoF", "threeF", "fourF", "fiveF", "sixF", "sevenF", "eightF", "nineF"};
    private final WordPair[] pairs = new WordPair[nativeWords.length];

    @Before
    public void prep() {
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = new WordPair(nativeWords[i], foreignWords[i]);
        }
    }

    @Test
    public void getNativeWord() {
        for (int i = 0; i < pairs.length; i++) {
            assertEquals(nativeWords[i], pairs[i].getNativeWord());
        }
    }

    @Test
    public void getForeignWord() {
        for (int i = 0; i < pairs.length; i++) {
            assertEquals(foreignWords[i], pairs[i].getForeignWord());
        }
    }

    @Test
    public void setNativeWord() {
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].setNativeWord(nativeWords[(i+1) % N]);
            assertEquals(nativeWords[(i+1) % N], pairs[i].getNativeWord());
        }
    }

    @Test
    public void setForeignWord() {
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].setForeignWord(foreignWords[(i+1) % N]);
            assertEquals(foreignWords[(i+1) % N], pairs[i].getForeignWord());
        }
    }
}