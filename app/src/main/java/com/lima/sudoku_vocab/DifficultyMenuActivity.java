package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.lima.model.VocabSudokuBoard;

public class DifficultyMenuActivity extends AppCompatActivity {

    public static final String GAME_MODE_CODE_MENU = "com.lima.sudoku_vocab.DifficultyMenuActivity - Game Mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_menu);

        setUpDifficultyButtons();
    }

    private void setUpDifficultyButtons() {
        Button easyBtn = findViewById(R.id.easy_btn);
        easyBtn.setOnClickListener(
                v -> onDifficultyButtonClick(VocabSudokuBoard.DIFFICULTY_EASY)
        );

        Button mediumBtn = findViewById(R.id.medium_btn);
        mediumBtn.setOnClickListener(
                v -> onDifficultyButtonClick(VocabSudokuBoard.DIFFICULTY_MEDIUM)
        );

        Button hardBtn = findViewById(R.id.hard_btn);
        hardBtn.setOnClickListener(
                v -> onDifficultyButtonClick(VocabSudokuBoard.DIFFICULTY_HARD)
        );
    }

    private void onDifficultyButtonClick(float difficulty) {

        //Default value is classic mode
        int mode = getIntent().getIntExtra(GAME_MODE_CODE_MENU, SudokuGameActivity.CLASSIC_MODE);
        Intent intent = SudokuGameActivity.makeIntent(this, mode, difficulty);
        startActivity(intent);
        finish();
    }

    public static Intent makeIntent(Context context, int gameMode) {
        Intent intent = new Intent(context, DifficultyMenuActivity.class);
        intent.putExtra(GAME_MODE_CODE_MENU, gameMode);
        return intent;
    }
}