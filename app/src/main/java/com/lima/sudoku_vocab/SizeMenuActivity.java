package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.lima.model.VocabSudokuBoard;

public class SizeMenuActivity extends AppCompatActivity {

    public static final String GAME_MODE_CODE_SIZE_MENU = "com.lima.sudoku_vocab.SizeMenuActivity - Game Mode";
    public static final String DIFFICULTY_CODE_SIZE_MENU = "com.lima.sudoku_vocab.SizeMenuActivity - Difficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_menu);

        setUpButtons();
    }

    private void setUpButtons() {
        Button fourBtn = findViewById(R.id.four_btn);
        Button sixBtn = findViewById(R.id.six_btn);
        Button nineBtn = findViewById(R.id.nine_btn);
        Button twelveBtn = findViewById(R.id.twelve_btn);
        
        fourBtn.setOnClickListener(
                v -> onClick_sizeButton(VocabSudokuBoard.FOUR_DIMENSION)
        );

        sixBtn.setOnClickListener(
                v -> onClick_sizeButton(VocabSudokuBoard.SIX_DIMENSION)
        );

        nineBtn.setOnClickListener(
                v -> onClick_sizeButton(VocabSudokuBoard.DEFAULT_DIMENSION)
        );

        twelveBtn.setOnClickListener(
                v -> onClick_sizeButton(VocabSudokuBoard.TWELVE_DIMENSION)
        );
    }

    private void onClick_sizeButton(int size) {
        Intent oldIntent = getIntent();
        int mode = oldIntent.getIntExtra(GAME_MODE_CODE_SIZE_MENU, SudokuGameActivity.CLASSIC_MODE);
        float difficulty = oldIntent.getFloatExtra(DIFFICULTY_CODE_SIZE_MENU, VocabSudokuBoard.DIFFICULTY_EASY);

        Intent newIntent = WordSelectActivity.makeIntent(this, mode, difficulty, size);
        startActivity(newIntent);
        finish();
    }

    public static Intent makeIntent(Context context, int mode, float difficulty) {
        Intent intent = new Intent(context, SizeMenuActivity.class);
        intent.putExtra(GAME_MODE_CODE_SIZE_MENU, mode);
        intent.putExtra(DIFFICULTY_CODE_SIZE_MENU, difficulty);
        return intent;
    }
}