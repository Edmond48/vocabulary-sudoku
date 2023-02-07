package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lima.model.VocabSudokuBoard;

import org.w3c.dom.Text;

public class SudokuGameActivity extends AppCompatActivity {

    public static final double WORDS_PER_ROW = 3.0;
    VocabSudokuBoard board = new VocabSudokuBoard();

    private final int BOARD_SIDE = board.getDimension();
    private TextView[][] cells = new TextView[BOARD_SIDE][BOARD_SIDE];

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
            table.addView(row);
            for (int j = 0; j < BOARD_SIDE; j++) {
                final int rowIndex = i;
                final int colIndex = j;

                // TODO Refactor to a function
                // Setup cell
                TextView cell = new TextView(this);
                cell.setText("Test");
                cell.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                // Checker board pattern
                if ((i+j)%2 == 0)
                    cell.setBackgroundColor(Color.YELLOW);
                else
                    cell.setBackgroundColor(Color.BLUE);

                // Toast the coordinate when a cell is clicked
                cell.setOnClickListener(view -> {
                    Toast.makeText(
                        SudokuGameActivity.this,
                        "You clicked the cell at (" + rowIndex + ", " + colIndex + ")",
                        Toast.LENGTH_SHORT).show();
                    Log.i("GameActivity", "cell clicked");
                });

                // Set the cell's height to match its width after it has been drawn on the screen
                makeTextViewSquare(cell);

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
            wordButtons.addView(row);

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            for (int j = 0; j < WORDS_PER_ROW && j + i * WORDS_PER_ROW < BOARD_SIDE; j++) {
                final int rowIndex = i;
                final int colIndex = j;

                TextView wordBtn = new TextView(this);

                wordBtn.setText(board.getWord(colIndex + rowIndex * (int) WORDS_PER_ROW).getNativeWord());
                wordBtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                wordBtn.setBackgroundColor(Color.GRAY);
                wordBtn.setPadding(0, 10, 0, 10);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                params.setMargins(10, 10, 10, 10);
                wordBtn.setLayoutParams(params);

                wordBtn.setOnClickListener(view -> {
                    Toast.makeText(
                            this,
                            "You press the word: " + board.getWord(colIndex + rowIndex * (int) WORDS_PER_ROW).getNativeWord(),
                            Toast.LENGTH_SHORT).show();
                });

                row.addView(wordBtn);
            }
        }
    }

    private void makeTextViewSquare(TextView cell) {
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
            }
        });
    }
}