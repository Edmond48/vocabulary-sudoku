package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setUpButtons();
    }

    private void setUpButtons() {
        Button classicBtn = findViewById(R.id.classic_mode_btn);
        classicBtn.setOnClickListener(
                v -> onGameModeButtonClick(SudokuGameActivity.CLASSIC_MODE)
        );

        Button reverseBtn = findViewById(R.id.reverse_mode_btn);
        reverseBtn.setOnClickListener(
                v -> onGameModeButtonClick(SudokuGameActivity.REVERSE_MODE)
        );

        Button listenBtn = findViewById(R.id.listen_mode_btn);
        listenBtn.setOnClickListener(
                v -> onGameModeButtonClick(SudokuGameActivity.LISTEN_MODE)
        );

        Button wordListBtn = findViewById(R.id.word_list_btn);
        wordListBtn.setOnClickListener(
                v -> onWordListButtonClick()
        );
    }

    private void onWordListButtonClick() {
        startActivity(WordListActivity.makeIntent(this));
    }

    private void onGameModeButtonClick(int mode) {
        Intent intent = DifficultyMenuActivity.makeIntent(this, mode);
        startActivity(intent);
    }
}