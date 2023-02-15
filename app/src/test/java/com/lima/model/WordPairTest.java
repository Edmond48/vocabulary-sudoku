package com.lima.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WordPairTest {

    @Test
    void testWordPairCreation() {
        WordPair pair = new WordPair("car", "xe");
        assertEquals("car", pair.getNativeWord());
        assertEquals("xe", pair.getForeignWord());
    }

//    @Test
//    void setNativeWord() {
//    }
//
//    @Test
//    void getForeignWord() {
//    }
//
//    @Test
//    void setForeignWord() {
//    }
//
//    @Test
//    void main() {
//    }
}