package com.lima.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class WordPairTest {

    // Tests if expected native and foreign words are returned for the word pair
    @Test
    public void testWordPairCreation() {
        WordPair pair = new WordPair("car", "xe");
        assertEquals("car", pair.getNativeWord());
        assertEquals("xe", pair.getForeignWord());
    }
}