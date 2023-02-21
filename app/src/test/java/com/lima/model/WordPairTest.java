package com.lima.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class WordPairTest {

    private WordPair pair = new WordPair("car", "xe");

    // Tests if expected native and foreign words are returned for the word pair
    @Test
    public void testWordPairCreation() {
        assertEquals("car", pair.getNativeWord());
        assertEquals("xe", pair.getForeignWord());
    }

    @Test
    public void setNativeWord() {
        pair.setNativeWord("home");
        assertEquals("home", pair.getNativeWord());
    }

    @Test
    public void setForeignWord() {
        pair.setForeignWord("nha");
        assertEquals("nha",pair.getForeignWord());
    }

}