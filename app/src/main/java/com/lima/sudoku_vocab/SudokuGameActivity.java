package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lima.model.VocabSudokuBoard;
import com.lima.model.WordPair;

public class SudokuGameActivity extends AppCompatActivity {

    // constants for game modes
    // in controller because model classes should not need to know which mode is being used
    // the only effect of modes is which word is shown in UI, does not change game logic
    public static final int CLASSIC_MODE = 0;
    public static final int REVERSE_MODE = 1;
    public static final int LISTEN_MODE = 2;

    // codes to extract Extras from Intent used to launch the Activity
    public static final String GAME_MODE_CODE_IN_GAME = "com.lima.sudoku_vocab.SudokuGameActivity - Game mode";
    public static final String DIFFICULTY_CODE_IN_GAME = "com.lima.sudoku_vocab.SudokuGameActivity - Difficulty";
    public static final String SIZE_CODE_IN_GAME = "com.lima.sudoku_vocab.SudokuGameActivity - Size";
    public static final String NATIVE_WORDS_CODE_IN_GAME = "com.lima.sudoku_vocab.SudokuGameActivity - Native Words";
    public static final String FOREIGN_WORDS_CODE_IN_GAME = "com.lima.sudoku_vocab.SudokuGameActivity - Foreign Words";

    // colors for UI control
    private int PRIMARY_CELL_COLOR;
    private int SECONDARY_CELL_COLOR;
    private int FIXED_CELL_COLOR;
    private int HIGHLIGHT_COLOR;

    // board object
    VocabSudokuBoard board;

    // current activity's game mode
    private int gameMode;

    // board dimension
    private int boardSide;

    // 2D array for all TextView cells in the board
    private TextView[][] cells;

    // number of word buttons per row of display
    private double buttonsPerRow;

    // 1D array for TextView buttons
    private TextView[] wordButtons;

    // clear button
    private TextView clearButton;

    // the field for displaying a word pair
    private TextView displayWord;

    // index of the currently selected action button
    // 0 to (dimension - 1) corresponds with index of the word pair in board[][]
    // VocabSudokuBoard.EMPTY_WORD corresponds to clear button
    // BOARD_SIDE means no button is selected
    private int selectedActionButtonIndex;

    // cell goes from 0 to (dimension^2VocabSudokuBoard.EMPTY_WORD)
    // VocabSudokuBoard.EMPTY_WORD means no cell is selected
    private int selectedCellIndex = VocabSudokuBoard.EMPTY_WORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);

        configureMemberVariables();

        populateSudokuBoard();

        populateWordButtons();

        setUpDisplayWordField();

        setUpClearButton();
    }

    // extract the game mode and difficulty from the intent
    // set up member variables accordingly
    private void configureMemberVariables() {
        Intent intent = getIntent();

        // default game mode is classic
        this.gameMode = intent.getIntExtra(GAME_MODE_CODE_IN_GAME, CLASSIC_MODE);

        // default difficulty is easy
        this.board = new VocabSudokuBoard(
                intent.getFloatExtra(DIFFICULTY_CODE_IN_GAME, VocabSudokuBoard.DIFFICULTY_EASY),
                intent.getIntExtra(SIZE_CODE_IN_GAME, VocabSudokuBoard.DEFAULT_DIMENSION),
                intent.getStringArrayExtra(NATIVE_WORDS_CODE_IN_GAME),
                intent.getStringArrayExtra(FOREIGN_WORDS_CODE_IN_GAME)
                );

        this.boardSide = board.getDimension();

        // set up dimension for word buttons table
        switch (boardSide) {
            case VocabSudokuBoard.FOUR_DIMENSION:
                buttonsPerRow = 2;
                break;
            case VocabSudokuBoard.TWELVE_DIMENSION:
                buttonsPerRow = 4;
                break;
            default:
                buttonsPerRow = 3;
                break;
        }

        this.selectedActionButtonIndex = boardSide;

        this.cells = new TextView[boardSide][boardSide];

        this.wordButtons = new TextView[boardSide];

        PRIMARY_CELL_COLOR = ContextCompat.getColor(this, R.color.blue);
        FIXED_CELL_COLOR = ContextCompat.getColor(this, R.color.dark_blue);
        HIGHLIGHT_COLOR = ContextCompat.getColor(this, R.color.purple_500);
        SECONDARY_CELL_COLOR = ContextCompat.getColor(this, R.color.light_blue);
    }

    // Create a 9x9 board and populate it with TextView
    private void populateSudokuBoard() {
        TableLayout table = findViewById(R.id.sudoku_table);

        for (int i = 0; i < boardSide; i++) {

            // set up UI attributes for the row
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            table.addView(row);

            for (int j = 0; j < boardSide; j++) {
                final int rowIndex = i;
                final int colIndex = j;

                // Setup cell
                TextView cell = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );

                // set bigger margin to make the sub-grids more visible
                // solution is generalized for all board sizes
                int bigMarginValue = 5;
                int smallMarginValue = 2;
                int bottomMargin =
                        (i + 1) % board.getGridHeight() == 0 && i != boardSide ?
                                bigMarginValue :
                                smallMarginValue;
                int rightMargin =
                        (j + 1) % board.getGridWidth() == 0 && j != boardSide ?
                                bigMarginValue :
                                smallMarginValue;

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

    // set up the table containing buttons for entering words
    private void populateWordButtons() {
        TableLayout wordButtons = findViewById(R.id.word_buttons);

        for (int i = 0; i < Math.ceil(boardSide / buttonsPerRow); i++) {
            TableRow row = new TableRow(this);

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            wordButtons.addView(row);

            for (int j = 0; j < buttonsPerRow && j + i * buttonsPerRow < boardSide; j++) {

                TextView wordBtn = new TextView(this);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );

                params.setMargins(5, 5, 5, 5);
                wordBtn.setLayoutParams(params);

                wordBtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                wordBtn.setGravity(Gravity.CENTER);
                wordBtn.setBackgroundResource(R.drawable.rounded_corner_blue);

                final int wordIndex = j + i * (int) buttonsPerRow;

                wordBtn.setOnClickListener(
                        view -> onActionButtonClick(wordIndex)
                );

                setUpWordButtonWhenDrawn(wordBtn, i, j);

                row.addView(wordBtn);

                this.wordButtons[wordIndex] = wordBtn;
            }
        }
    }

    // assign the UI element to a variable for ease of access
    private void setUpDisplayWordField() {
        this.displayWord = findViewById(R.id.wordDisplay);
    }

    // set up clear button
    private void setUpClearButton() {
        this.clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> onActionButtonClick(VocabSudokuBoard.EMPTY_WORD));
    }

    // handles click on the Sudoku board's cell
    private void onCellClick(int rowIndex, int colIndex) {
        // If there is an action button already selected
        if (selectedActionButtonIndex < boardSide) {

            // change selected index
            selectedCellIndex = rowIndex * boardSide + colIndex;

            // If cell can be modified
            if (!board.isFixed(rowIndex, colIndex)) {
                // Insert the word inside data structure
                board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

                // Update word on screen
                cells[rowIndex][colIndex].setText(getPrimaryWord(board.getWord(selectedActionButtonIndex)));

                updateWordDisplay(rowIndex, colIndex);
            }

            // Deselected the cell and update selected index
            setCellBackgroundColor(rowIndex, colIndex);
            selectedCellIndex = VocabSudokuBoard.EMPTY_WORD;
        }
        // Only cell is selected
        else {
            // Unhighlight current selected cell if it exists
            if (selectedCellIndex != VocabSudokuBoard.EMPTY_WORD) {
                int currentSelectedRow = selectedCellIndex / boardSide;
                int currentSelectedCol = selectedCellIndex % boardSide;
                setCellBackgroundColor(currentSelectedRow, currentSelectedCol);
            }

            updateWordDisplay(rowIndex, colIndex);

            // If the current cell is already selected, unselect it
            if (rowIndex == selectedCellIndex / boardSide && colIndex == selectedCellIndex % boardSide) {
                selectedCellIndex = VocabSudokuBoard.EMPTY_WORD;
                return;
            }

            // Set and highlight new cell
            selectedCellIndex = rowIndex * boardSide + colIndex;
            cells[rowIndex][colIndex].setBackgroundColor(HIGHLIGHT_COLOR);
        }
    }

    // handles clicks on word buttons and the clear button
    private void onActionButtonClick(int wordIndex) {

        int rowIndex = selectedCellIndex / boardSide;
        int colIndex = selectedCellIndex % boardSide;

        // If there is a cell already selected
        if (selectedCellIndex > VocabSudokuBoard.EMPTY_WORD) {

            selectedActionButtonIndex = wordIndex;

            // If cell can be modified
            if (!board.isFixed(rowIndex, colIndex)) {
                // Set word inside data structure
                board.setCell(rowIndex, colIndex, selectedActionButtonIndex);

                // Update the word on the screen
                cells[rowIndex][colIndex].setText(getPrimaryWord(board.getWord(selectedActionButtonIndex)));

                updateWordDisplay(rowIndex, colIndex);
            }

            // Reset both buttons
            setActionButtonBackgroundDefault(selectedActionButtonIndex);
            setCellBackgroundColor(rowIndex, colIndex);
            selectedActionButtonIndex = boardSide;
            selectedCellIndex = VocabSudokuBoard.EMPTY_WORD;

        }
        // Only action button is selected
        else {
            // Un-highlight action button
            if (selectedActionButtonIndex < boardSide)
                setActionButtonBackgroundDefault(selectedActionButtonIndex);

            // If the action button is already selected, unselect it
            if (selectedActionButtonIndex == wordIndex) {
                selectedActionButtonIndex = boardSide;
                return;
            }

            // Set new word button and highlight it
            selectedActionButtonIndex = wordIndex;
            setActionButtonBackGroundSelected(selectedActionButtonIndex);
        }
    }

    // fill cells with their words after the view is inflated on the screen
    // Attempts to lock the dimensions of the cells to make them square
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
                if (wordIndex != VocabSudokuBoard.EMPTY_WORD)
                    cell.setText(getSecondaryWord(board.getWord(wordIndex)));

                // make the cell square
                cell.setHeight(cell.getWidth());

                cell.setMaxWidth(cell.getWidth());
                cell.setMinimumWidth(cell.getWidth());

                cell.setMaxHeight(cell.getHeight());
                cell.setMinimumHeight(cell.getHeight());

                // set correct background color
                setCellBackgroundColor(row, col);

                // make the text truncated if it exceeds the cell's size
                cell.setEllipsize(TextUtils.TruncateAt.MARQUEE);
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
                wordBtn.setText(getPrimaryWord(board.getWord(colIndex + rowIndex * (int) buttonsPerRow)));
            }
        });
    }

    // returns true if the sub-grid should be colored light blue
    private boolean isInLightSubgrid(int row, int col) {
        int subgridRow = row / board.getGridHeight();
        int subgridColumn = col / board.getGridWidth();
        return (subgridRow + subgridColumn) % 2 == 1;
    }

    // default cells are blue
    // selected cells are purple
    // fixed cells are dark blue
    private void setCellBackgroundColor(int row, int col) {
        if (board.isFixed(row, col))
            cells[row][col].setBackgroundColor(FIXED_CELL_COLOR);
        else if (isInLightSubgrid(row, col))
            cells[row][col].setBackgroundColor(SECONDARY_CELL_COLOR);
        else
            cells[row][col].setBackgroundColor(PRIMARY_CELL_COLOR);
    }

    // set word buttons and clear button to default background
    private void setActionButtonBackgroundDefault(int actionIndex) {
        if (actionIndex == VocabSudokuBoard.EMPTY_WORD)
            clearButton.setBackgroundResource(R.drawable.backspace_background_blue);
        else
            wordButtons[actionIndex].setBackgroundResource(R.drawable.rounded_corner_blue);
    }

    // set word buttons and clear buttons to selected background
    private void setActionButtonBackGroundSelected(int actionIndex) {
        if (actionIndex == VocabSudokuBoard.EMPTY_WORD)
            clearButton.setBackgroundResource(R.drawable.backspace_background_purple);
        else
            wordButtons[actionIndex].setBackgroundResource(R.drawable.rounded_corner_purple);
    }

    // update the word display field with the word pair from selected cell
    // or clear the field
    private void updateWordDisplay(int row, int col) {
        StringBuilder builder = new StringBuilder();

        int wordIndex = board.getCell(row, col);

        // if cell is empty
        if (wordIndex == VocabSudokuBoard.EMPTY_WORD) {
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

    // words users will use to fill in
    private String getPrimaryWord(WordPair pair) {
        return gameMode == CLASSIC_MODE ? pair.getForeignWord() : pair.getNativeWord();
    }

    // words pre-filled by the game
    private String getSecondaryWord(WordPair pair) {
        return gameMode == CLASSIC_MODE ? pair.getNativeWord() : pair.getForeignWord();
    }

    // make an Intent to launch this activity
    public static Intent makeIntent(Context context, int mode, float difficulty, int size, String[] nativeWords, String[] foreignWords) {
        Intent intent = new Intent(context, SudokuGameActivity.class);
        intent.putExtra(GAME_MODE_CODE_IN_GAME, mode);
        intent.putExtra(DIFFICULTY_CODE_IN_GAME, difficulty);
        intent.putExtra(SIZE_CODE_IN_GAME, size);
        intent.putExtra(NATIVE_WORDS_CODE_IN_GAME, nativeWords);
        intent.putExtra(FOREIGN_WORDS_CODE_IN_GAME, foreignWords);
        return intent;
    }
}