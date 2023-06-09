package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lima.model.VocabSudokuBoard;
import com.lima.model.WordPair;

import java.util.ArrayList;

public class WordSelectActivity extends AppCompatActivity {

    public static final String GAME_MODE_CODE_SELECT_WORD = "com.lima.sudoku_vocab.WordSelectActivity - Game Mode";
    public static final String DIFFICULTY_CODE_SELECT_WORD = "com.lima.sudoku_vocab.WordSelectActivity - Difficulty";
    public static final String SIZE_CODE_SELECT_WORD = "com.lima.sudoku_vocab.WordSelectActivity - Size";

    private int boardDimension;
    private String[] wordPairStrings;
    private ArrayList<WordPair> pairList;

    private ListView wordList;
    private TextView selectedWordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_select);

        configureViews();
        getDataFromDB();
        populateWordList();
    }

    private void configureViews() {
        boardDimension = getIntent().getIntExtra(SIZE_CODE_SELECT_WORD, VocabSudokuBoard.DEFAULT_DIMENSION);

        wordList = findViewById(R.id.word_select_list);
        Button confirmBtn = findViewById(R.id.confirmBtn);

        selectedWordCount = findViewById(R.id.selected_word_count);
        selectedWordCount.setText(
                getStringForPairCount()
        );

        confirmBtn.setOnClickListener(
                v -> onConfirmButtonClick()
        );
    }

    // get word list from DB and build local array
    // this Activity does not modify the DB itself
    // so it is easier to extract available word pairs and work with them locally
    private void getDataFromDB() {
        DBAdapter wordDB = new DBAdapter(this);
        wordDB.open();
        Cursor cursor = wordDB.getAllRows();

        pairList = new ArrayList<>();

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

        wordPairStrings = new String[pairList.size()];

        for (int i = 0; i < pairList.size(); i++) {
            wordPairStrings[i] = pairList.get(i).getNativeWord() + " - " + pairList.get(i).getForeignWord();
        }
    }

    // display available word pairs to choose from
    private void populateWordList() {
        // allows multiple items to be checked
        wordList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // use built-in simple adapter
        wordList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, wordPairStrings));

        // update current number of selected words
        wordList.setOnItemClickListener((parent, viewCLicked, screenPos, id) -> selectedWordCount.setText(
                getStringForPairCount()
        ));
    }

    // build string for the number of selected word pairs
    private String getStringForPairCount() {
        return getResources().getString(
                R.string.selected_word_count,
                wordList.getCheckedItemCount(),
                boardDimension);
    }

    private void onConfirmButtonClick() {
        int selectedCount = wordList.getCheckedItemCount();

        // if user select less or more word pairs than needed
        if (selectedCount != boardDimension) {
            Toast.makeText(this, "Invalid number of items selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nativeWords = new String[selectedCount];
        String[] foreignWords = new String[selectedCount];

        SparseBooleanArray booleanArray = wordList.getCheckedItemPositions();

        // build nativeWords and foreignWords list to pass to game Activity
        for (int i = 0, j = 0; i < pairList.size(); i++) {
            if (booleanArray.get(i)) {
                nativeWords[j] = pairList.get(i).getNativeWord();
                foreignWords[j] = pairList.get(i).getForeignWord();
                j++;
            }
        }

        // get info passed to this Activity
        Intent originalIntent = getIntent();

        int mode = originalIntent.getIntExtra(GAME_MODE_CODE_SELECT_WORD, SudokuGameActivity.CLASSIC_MODE);
        float difficulty = originalIntent.getFloatExtra(DIFFICULTY_CODE_SELECT_WORD, VocabSudokuBoard.DIFFICULTY_EASY);

        // build Intent and launch game activity
        Intent newIntent = SudokuGameActivity.makeIntent(this, mode, difficulty, boardDimension, nativeWords, foreignWords);
        startActivity(newIntent);

        // destroy this activity
        finish();
    }

    // make an Intent to launch this activity
    public static Intent makeIntent(Context context, int mode, float difficulty, int size) {
        Intent intent = new Intent(context, WordSelectActivity.class);
        intent.putExtra(GAME_MODE_CODE_SELECT_WORD, mode);
        intent.putExtra(DIFFICULTY_CODE_SELECT_WORD, difficulty);
        intent.putExtra(SIZE_CODE_SELECT_WORD, size);
        return intent;
    }
}