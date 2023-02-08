package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lima.model.VocabSudokuBoard;

public class SudokuGameActivity extends AppCompatActivity {

    public static final double WORDS_PER_ROW = 3.0;
    VocabSudokuBoard board = new VocabSudokuBoard();

    private final int BOARD_SIDE = board.getDimension();

    private final TextView[][] cells = new TextView[BOARD_SIDE][BOARD_SIDE];
    private final TextView[] wordButtons = new TextView[BOARD_SIDE];

    private int selectedActionButtonIndex = BOARD_SIDE;
    private int selectedCellIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        populateSudokuBoard();
        populateWordButtons();
    }

    // TODO When model classes are ready, incorporate them here
    // Create a 9x9 board and populate it with TextView
    private void populateSudokuBoard() {
        TableLayout table = findViewById(R.id.sudoku_table);

        for (int i = 0; i < BOARD_SIDE; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            table.addView(row);
            for (int j = 0; j < BOARD_SIDE; j++) {
                final int rowIndex = i;
                final int colIndex = j;

                // TODO Refactor to a function
                // Setup cell
                TextView cell = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                params.setMargins(5, 5, 5, 5);
                cell.setLayoutParams(params);

                cell.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundColor(Color.GRAY);
                cell.setPadding(1, 1, 1, 1);

                // Toast the coordinate when a cell is clicked
                cell.setOnClickListener(
                        view -> onCellClick(rowIndex, colIndex)
                );

                // Set the cell's height to match its width after it has been drawn on the screen
                setUpCellWhenDrawn(cell);

                // Add the cell to the row
                row.addView(cell);

                // Add the cell to internal array
                cells[i][j] = cell;
            }
        }
    }

    private void populateWordButtons() {
        TableLayout wordButtons = findViewById(R.id.word_buttons);
        wordButtons.setBackgroundColor(Color.BLACK);

        for (int i = 0; i < Math.ceil(BOARD_SIDE / WORDS_PER_ROW); i++) {
            TableRow row = new TableRow(this);

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            wordButtons.addView(row);

            for (int j = 0; j < WORDS_PER_ROW && j + i * WORDS_PER_ROW < BOARD_SIDE; j++) {
                final int rowIndex = i;
                final int colIndex = j;

                TextView wordBtn = new TextView(this);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                params.setMargins(5, 5, 5, 5);
                wordBtn.setLayoutParams(params);

                wordBtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                wordBtn.setBackgroundColor(Color.GRAY);

                final int wordIndex = j + i * (int) WORDS_PER_ROW;

                wordBtn.setOnClickListener(
                        view -> onActionButtonClick(wordIndex)
                );

                setUpWordButtonWhenDrawn(wordBtn, rowIndex, colIndex);

                row.addView(wordBtn);

                this.wordButtons[wordIndex] = wordBtn;
            }
        }
    }

    private void onCellClick(int rowIndex, int colIndex) {
        // If there is an action button already selected
        if (selectedActionButtonIndex < BOARD_SIDE) {

            selectedCellIndex = rowIndex * BOARD_SIDE + colIndex;

            // Insert the word inside data structure
            board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

            // Update word on screen
            cells[rowIndex][colIndex].setText(board.getWord(selectedActionButtonIndex).getNativeWord());

            // Deselected both and update indexes
            wordButtons[selectedActionButtonIndex].setBackgroundColor(Color.GRAY);
            cells[rowIndex][colIndex].setBackgroundColor(Color.GRAY);
            selectedActionButtonIndex = BOARD_SIDE;
            selectedCellIndex = -1;
        }
        else {
            // Unhighlight current selected cell if it exists
            if (selectedCellIndex != -1)
                cells[selectedCellIndex/BOARD_SIDE][selectedCellIndex%BOARD_SIDE].setBackgroundColor(Color.GRAY);

            if (rowIndex == selectedCellIndex / BOARD_SIDE && colIndex == selectedCellIndex % BOARD_SIDE) {
                selectedCellIndex = -1;
                return;
            }

            // Set and highlight new cell
            selectedCellIndex = rowIndex * BOARD_SIDE + colIndex;
            cells[rowIndex][colIndex].setBackgroundColor(Color.GREEN);
        }
    }

    private void onActionButtonClick(int wordIndex) {
        // If there is a cell already selected
        if (selectedCellIndex > -1) {
            int rowIndex = selectedCellIndex / BOARD_SIDE;
            int colIndex = selectedCellIndex % BOARD_SIDE;

            selectedActionButtonIndex = wordIndex;

            // Set word inside data structure
            board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

            // Update the word on the screen
            cells[rowIndex][colIndex].setText(board.getWord(selectedActionButtonIndex).getNativeWord());

            // Reset both buttons
            wordButtons[selectedActionButtonIndex].setBackgroundColor(Color.GRAY);
            cells[rowIndex][colIndex].setBackgroundColor(Color.GRAY);
            selectedActionButtonIndex = BOARD_SIDE;
            selectedCellIndex = -1;

        }
        else {
            // Deselect action button
            if (selectedActionButtonIndex < BOARD_SIDE && selectedActionButtonIndex > -1)
                wordButtons[selectedActionButtonIndex].setBackgroundColor(Color.GRAY);

            if (selectedActionButtonIndex == wordIndex) {
                selectedActionButtonIndex = BOARD_SIDE;
                return;
            }

            // Set new word button and highlight it
            selectedActionButtonIndex = wordIndex;
            wordButtons[selectedActionButtonIndex].setBackgroundColor(Color.GREEN);
        }
    }

    private void setUpCellWhenDrawn(TextView cell) {
        cell.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            // Handle correct method call on older Android versions
            @SuppressLint("NewAPI")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    cell.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    cell.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // make the cell square
                cell.setHeight(cell.getWidth());

                cell.setMaxWidth(cell.getWidth());
                cell.setMinimumWidth(cell.getWidth());

                cell.setMaxHeight(cell.getHeight());
                cell.setMinimumHeight(cell.getHeight());

                cell.setBackgroundColor(Color.GRAY);
                cell.setMaxLines(1);
            }
        });
    }

    private void setUpWordButtonWhenDrawn(TextView wordBtn, int rowIndex, int colIndex) {
        wordBtn.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            // Handle correct method call on older Android versions
            @SuppressLint("NewAPI")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    wordBtn.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    wordBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // lock the width and the height
                wordBtn.setMaxWidth(wordBtn.getWidth());
                wordBtn.setMinimumWidth(wordBtn.getWidth());

                wordBtn.setMaxHeight(wordBtn.getHeight());
                wordBtn.setMinimumHeight(wordBtn.getHeight());

                // set the text
                wordBtn.setText(board.getWord(colIndex + rowIndex * (int) WORDS_PER_ROW).getNativeWord());
            }
        });
    }
}