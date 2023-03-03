package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lima.model.WordPair;

import java.util.ArrayList;

public class WordSelectActivity extends AppCompatActivity {

    // TODO
    //  - set up onclick behavior of list items
    //  - report current total selected words
    //  - make confirm button
    //  - connect activity to game

    public static final String GAME_MODE_CODE_SELECT_WORD = "com.lima.sudoku_vocab.WordSelectActivity - Mode";
    public static final String DIFFICULTY_CODE_SELECT_WORD = "com.lima.sudoku_vocab.WordSelectActivity - Difficulty";

    private String[] nativeWords;
    private String[] foreignWords;
    private String[] wordPairs;

    private ListView wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_select);

        configureViews();
        getDataFromDB();
        populateWordList();
    }

    private void configureViews() {
        wordList = findViewById(R.id.word_select_list);
    }

    private void getDataFromDB() {
        DBAdapter wordDB = new DBAdapter(this);
        wordDB.open();
        Cursor cursor = wordDB.getAllRows();

        ArrayList<WordPair> pairList = new ArrayList<WordPair>();

        if (cursor.moveToFirst()) {
            do {
                pairList.add(
                        new WordPair(
                                cursor.getString(DBAdapter.COL_NATIVE_WORD),
                                cursor.getString(DBAdapter.COL_FOREIGN_WORD)
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        wordDB.close();

        nativeWords = new String[pairList.size()];
        foreignWords = new String[pairList.size()];
        wordPairs = new String[pairList.size()];

        for (int i = 0; i < pairList.size(); i++) {
            nativeWords[i] = pairList.get(i).getForeignWord();
            foreignWords[i] = pairList.get(i).getNativeWord();
            wordPairs[i] = nativeWords[i] + " - " + foreignWords[i];
        }
    }

    private void populateWordList() {
        wordList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        wordList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, wordPairs));
    }

    public static Intent makeIntent(Context context, int mode, float difficulty) {
        Intent intent = new Intent(context, WordSelectActivity.class);
        intent.putExtra(GAME_MODE_CODE_SELECT_WORD, mode);
        intent.putExtra(DIFFICULTY_CODE_SELECT_WORD, difficulty);
        return intent;
    }
}