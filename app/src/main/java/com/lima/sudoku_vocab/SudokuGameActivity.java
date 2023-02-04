package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SudokuGameActivity extends AppCompatActivity {

    private static final int BOARD_SIDE = 9;
    private TextView[][] cells = new TextView[BOARD_SIDE][BOARD_SIDE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        populateSudokuBoard();
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
                cell.setOnClickListener(view -> Toast.makeText(
                        SudokuGameActivity.this,
                        "You clicked the cell at (" + rowIndex + ", " + colIndex + ")",
                        Toast.LENGTH_SHORT).show());

                // Set the cell's height to match its width after it has been drawn on the screen
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

                // Add the cell to the row
                row.addView(cell);

                // Add the cell to internal array
                cells[i][j] = cell;
            }
        }
    }
}