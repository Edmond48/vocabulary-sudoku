package com.lima.model;

public class WordPair {
    private String nativeWord;
    private String foreignWord;

    public WordPair(String nativeWord, String foreignWord) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
    }

    // getters and setters
    public String getNativeWord() {
        return nativeWord;
    }

    public void setNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public String getForeignWord() {
        return foreignWord;
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }

    // main for testing
    public static void main(String[] args) {
        WordPair pair = new WordPair("Dragon", "Rồng");
        System.out.println(pair.getNativeWord() + " " + pair.getForeignWord());
    }
}