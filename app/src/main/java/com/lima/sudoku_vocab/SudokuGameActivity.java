package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lima.model.VocabSudokuBoard;
import com.lima.model.WordPair;

import org.w3c.dom.Text;

public class SudokuGameActivity extends AppCompatActivity {

    // number of word buttons per row
    public static final double WORDS_PER_ROW = 3.0;

    // colors for UI control
    private int PRIMARY_CELL_COLOR;
    private int SECONDARY_CELL_COLOR;
    private int FIXED_CELL_COLOR;
    private int HIGHLIGHT_COLOR;
    
    // board object
    VocabSudokuBoard board = new VocabSudokuBoard();

    // board dimension
    private final int BOARD_SIDE = board.getDimension();

    // 2D array for all TextView cells
    private final TextView[][] cells = new TextView[BOARD_SIDE][BOARD_SIDE];

    // 1D array for TextView buttons
    private final TextView[] wordButtons = new TextView[BOARD_SIDE];

    // clear button
    private TextView clearButton;

    // the field for displaying a word pair
    private TextView displayWord;

    // index of the currently selected action button
    // 0 to (dimension - 1) corresponds with index of the word pair in board[][]
    // -1 corresponds to clear button
    // BOARD_SIDE means no button is selected
    private int selectedActionButtonIndex = BOARD_SIDE;

    // cell goes from 0 to (dimension^2-1)
    // -1 means no cell is selected
    private int selectedCellIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        PRIMARY_CELL_COLOR = ContextCompat.getColor(this, R.color.blue);
        FIXED_CELL_COLOR = ContextCompat.getColor(this, R.color.dark_blue);
        HIGHLIGHT_COLOR = ContextCompat.getColor(this, R.color.purple_500);
        SECONDARY_CELL_COLOR = ContextCompat.getColor(this, R.color.light_blue);

        populateSudokuBoard();
        populateWordButtons();
        setUpDisplayWordField();
        setUpClearButton();
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

                // hacky solution to create border around sub-grids
                // TODO generalize the sub-grid display to work
                int bigMarginValue = 10;
                int smallMarginValue = 2;
                int bottomMargin = i == 2 || i == 5 ? bigMarginValue : smallMarginValue;
                int rightMargin = j == 2 || j == 5 ? bigMarginValue : smallMarginValue;
                params.setMargins(smallMarginValue, smallMarginValue, rightMargin, bottomMargin);
                cell.setLayoutParams(params);

                cell.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                cell.setGravity(Gravity.CENTER);

                cell.setPadding(1, 1, 1, 1);

                // set up onClick behavior
                cell.setOnClickListener(
                        view -> onCellClick(rowIndex, colIndex)
                );

                // Set the cell's height to match its width after it has been drawn on the screen
                setUpCellWhenDrawn(cell, rowIndex, colIndex);

                // Add the cell to the row
                row.addView(cell);

                // Add the cell to internal array
                cells[i][j] = cell;
            }
        }
    }

    private void populateWordButtons() {
        TableLayout wordButtons = findViewById(R.id.word_buttons);

        for (int i = 0; i < Math.ceil(BOARD_SIDE / WORDS_PER_ROW); i++) {
            TableRow row = new TableRow(this);

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            wordButtons.addView(row);

            for (int j = 0; j < WORDS_PER_ROW && j + i * WORDS_PER_ROW < BOARD_SIDE; j++) {

                TextView wordBtn = new TextView(this);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );

                params.setMargins(5, 5, 5, 5);
                wordBtn.setLayoutParams(params);

                wordBtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                wordBtn.setGravity(Gravity.CENTER);
                wordBtn.setBackgroundResource(R.drawable.rounded_corner_blue);

                final int wordIndex = j + i * (int) WORDS_PER_ROW;

                wordBtn.setOnClickListener(
                        view -> onActionButtonClick(wordIndex)
                );

                setUpWordButtonWhenDrawn(wordBtn, i, j);

                row.addView(wordBtn);

                this.wordButtons[wordIndex] = wordBtn;
            }
        }
    }


    private void setUpDisplayWordField() {
        this.displayWord = findViewById(R.id.wordDisplay);
    }

    private void setUpClearButton() {
        this.clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> onActionButtonClick(-1));
    }

    private void onCellClick(int rowIndex, int colIndex) {
        // If there is an action button already selected
        if (selectedActionButtonIndex < BOARD_SIDE) {

            // change selected index
            selectedCellIndex = rowIndex * BOARD_SIDE + colIndex;

            // If cell can be modified
            if (!board.isFixed(rowIndex, colIndex)) {
                // Insert the word inside data structure
                board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

                // Update word on screen
                cells[rowIndex][colIndex].setText(board.getWord(selectedActionButtonIndex).getNativeWord());

                updateWordDisplay(rowIndex, colIndex);
            }

            // Deselected the cell and update selected index
            setCellBackgroundColor(rowIndex, colIndex);
            selectedCellIndex = -1;
        }
        // Only cell is selected
        else {
            // Unhighlight current selected cell if it exists
            if (selectedCellIndex != -1) {
                int currentSelectedRow = selectedCellIndex / BOARD_SIDE;
                int currentSelectedCol = selectedCellIndex % BOARD_SIDE;
                setCellBackgroundColor(currentSelectedRow, currentSelectedCol);
            }

            updateWordDisplay(rowIndex, colIndex);

            // If the current cell is already selected, unselect it
            if (rowIndex == selectedCellIndex / BOARD_SIDE && colIndex == selectedCellIndex % BOARD_SIDE) {
                selectedCellIndex = -1;
                return;
            }

            // Set and highlight new cell
            selectedCellIndex = rowIndex * BOARD_SIDE + colIndex;
            cells[rowIndex][colIndex].setBackgroundColor(HIGHLIGHT_COLOR);
        }
    }

    private void onActionButtonClick(int wordIndex) {

        int rowIndex = selectedCellIndex / BOARD_SIDE;
        int colIndex = selectedCellIndex % BOARD_SIDE;

        // If there is a cell already selected
        if (selectedCellIndex > -1) {

            selectedActionButtonIndex = wordIndex;

            // If cell can be modified
            if (!board.isFixed(rowIndex, colIndex)) {
                // Set word inside data structure
                board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

                // Update the word on the screen
                cells[rowIndex][colIndex].setText(board.getWord(selectedActionButtonIndex).getNativeWord());

                updateWordDisplay(rowIndex, colIndex);
            }

            // Reset both buttons
            setActionButtonBackgroundDefault(selectedActionButtonIndex);
            setCellBackgroundColor(rowIndex, colIndex);
            selectedActionButtonIndex = BOARD_SIDE;
            selectedCellIndex = -1;

        }
        // Only action button is selected
        else {
            // Un-highlight action button
            if (selectedActionButtonIndex < BOARD_SIDE)
                setActionButtonBackgroundDefault(selectedActionButtonIndex);

            // If the action button is already selected, unselect it
            if (selectedActionButtonIndex == wordIndex) {
                selectedActionButtonIndex = BOARD_SIDE;
                return;
            }

            // Set new word button and highlight it
            selectedActionButtonIndex = wordIndex;
            setActionButtonBackGroundSelected(selectedActionButtonIndex);
        }
    }

    private void setUpCellWhenDrawn(TextView cell, int row, int col) {
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

                // set text after board has been drawn
                int wordIndex = board.getCell(row, col);
                if (wordIndex != -1)
                    cell.setText(board.getWord(wordIndex).getNativeWord());

                // make the cell square
                cell.setHeight(cell.getWidth());

                cell.setMaxWidth(cell.getWidth());
                cell.setMinimumWidth(cell.getWidth());

                cell.setMaxHeight(cell.getHeight());
                cell.setMinimumHeight(cell.getHeight());

                setCellBackgroundColor(row, col);

                cell.setEllipsize(TextUtils.TruncateAt.END);
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

    private boolean isInLightSubgrid(int row, int col) {
        int subgridRow = row / board.getGridWidth();
        int subgridColumn= col / board.getGridHeight();
        Log.i("GameActivity", "row: " + row + ", col: " + col + " subgridRow: " + subgridRow + ", subgridColumn: " + subgridColumn);
        return (subgridRow + subgridColumn) % 2 == 1;
    }

    private void setCellBackgroundColor(int row, int col) {
        if (board.isFixed(row, col))
            cells[row][col].setBackgroundColor(FIXED_CELL_COLOR);
        else if (isInLightSubgrid(row, col))
            cells[row][col].setBackgroundColor(SECONDARY_CELL_COLOR);
        else
            cells[row][col].setBackgroundColor(PRIMARY_CELL_COLOR);
    }

    private void setActionButtonBackgroundDefault(int actionIndex) {
        if (actionIndex == -1)
            clearButton.setBackgroundResource(R.drawable.backspace_background_blue);
        else
            wordButtons[actionIndex].setBackgroundResource(R.drawable.rounded_corner_blue);
    }

    private void setActionButtonBackGroundSelected(int actionIndex) {
        if (actionIndex == -1)
            clearButton.setBackgroundResource(R.drawable.backspace_background_purple);
        else
            wordButtons[actionIndex].setBackgroundResource(R.drawable.rounded_corner_purple);
    }

    private void updateWordDisplay(int row, int col) {
        StringBuilder builder = new StringBuilder();

        int wordIndex = board.getCell(row, col);

        // if cell is empty
        if (wordIndex == -1) {
            displayWord.setText(builder.toString());
            return;
        }

        // display non-empty cell
        WordPair wordPair = board.getWord(wordIndex);
        builder.append(wordPair.getNativeWord());
        builder.append(" - ");
        builder.append(wordPair.getForeignWord());
        displayWord.setText(builder.toString());
    }
}