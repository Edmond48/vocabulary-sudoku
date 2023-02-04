package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SudokuGameActivity extends AppCompatActivity {

    // TODO for the whole file, replace hardcoded values
    TextView[][] cells = new TextView[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        populateSudokuBoard();
    }

    private void populateSudokuBoard() {
        TableLayout table = findViewById(R.id.sudoku_table);

        for (int i = 0; i < 9; i++) {
            TableRow row = new TableRow(this);
            table.addView(row);
            for (int j = 0; j < 9; j++) {
                final int rowIndex = i;
                final int colIndex = j;
                TextView cell = new TextView(this);
                cell.setText("Test");
                cell.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                if ((i+j)%2 == 0)
                    cell.setBackgroundColor(Color.YELLOW);
                else
                    cell.setBackgroundColor(Color.BLUE);

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(
                                SudokuGameActivity.this,
                                "You clicked the cell at (" + rowIndex + ", " + colIndex + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                cell.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressLint("NewAPI")
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            cell.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            cell.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        cell.setHeight(cell.getWidth());
                    }
                });
                row.addView(cell);
                cells[i][j] = cell;
            }
        }
    }
}