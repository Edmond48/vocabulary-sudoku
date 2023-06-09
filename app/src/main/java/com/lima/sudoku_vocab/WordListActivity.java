package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WordListActivity extends AppCompatActivity {

    public static final String TAG = "WordListActivity";
    DBAdapter wordDB;
    ListView wordList;

    TextView modeText;

    Button addBtn;
    Button removeBtn;

    Button confirmAddBtn;
    Button cancelAddBtn;

    long selectedRemoveItemId = -1;
    Button confirmRemoveBtn;
    Button cancelRemoveBtn;
    Button removeAllBtn;
    TextView removeInfo;

    EditText nativeWordEdit;
    EditText foreignWordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        openDB();
        configureViews();
        populateListViewFromDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        wordDB = new DBAdapter(this);
        wordDB.open();
    }

    private void closeDB() {
        wordDB.close();
    }

    private void configureViews() {
        modeText = findViewById(R.id.word_list_text);

        wordList = findViewById(R.id.word_list_view);

        addBtn = findViewById(R.id.addBtn);
        removeBtn = findViewById(R.id.removeBtn);

        confirmAddBtn = findViewById(R.id.confirmAddBtn);
        cancelAddBtn = findViewById(R.id.cancelAddBtn);

        confirmRemoveBtn = findViewById(R.id.confirmRemoveBtn);
        cancelRemoveBtn = findViewById(R.id.cancelRemoveBtn);
        removeAllBtn = findViewById(R.id.removeAllBtn);
        removeInfo = findViewById(R.id.remove_info_text);

        nativeWordEdit = findViewById(R.id.editNativeWord);
        foreignWordEdit = findViewById(R.id.editForeignWord);

        addBtn.setOnClickListener(
                v -> onAddButtonClick()
        );

        confirmAddBtn.setOnClickListener(
                v -> onConfirmAddButtonClick()
        );

        cancelAddBtn.setOnClickListener(
                v -> onCancelAddButtonClick()
        );

        removeBtn.setOnClickListener(
                v -> onRemoveButtonClick()
        );

        confirmRemoveBtn.setOnClickListener(
                v -> onConfirmRemoveButtonClick()
        );

        cancelRemoveBtn.setOnClickListener(
                v -> onCancelRemoveButtonClick()
        );

        removeAllBtn.setOnClickListener(
                v -> onRemoveAllButtonClick()
        );
    }

    // change to Add mode
    private void onAddButtonClick() {
        modeText.setText(R.string.add_mode);
        nativeWordEdit.setVisibility(View.VISIBLE);
        foreignWordEdit.setVisibility(View.VISIBLE);
        confirmAddBtn.setVisibility(View.VISIBLE);
        cancelAddBtn.setVisibility(View.VISIBLE);

        addBtn.setVisibility(View.GONE);
        removeBtn.setVisibility(View.GONE);
    }

    // change to Remove mode
    private void onRemoveButtonClick() {
        modeText.setText(R.string.remove_mode);
        removeAllBtn.setVisibility(View.VISIBLE);
        confirmRemoveBtn.setVisibility(View.VISIBLE);
        cancelRemoveBtn.setVisibility(View.VISIBLE);
        removeInfo.setVisibility(View.VISIBLE);

        addBtn.setVisibility(View.GONE);
        removeBtn.setVisibility(View.GONE);

        // set up onClick action for list items
        wordList.setOnItemClickListener((parent, viewClicked, position, idDB) -> {

            selectedRemoveItemId = idDB;
            updateRemoveInfoField(idDB);
        });
    }

    // update the information field with the pair selected for removal
    private void updateRemoveInfoField(long idDB) {
        Cursor cursor = wordDB.getRow(idDB);
        if (cursor.moveToFirst()) {
            removeInfo.setText(
                    getResources().getString(R.string.remove_info_pair,
                            cursor.getString(DBAdapter.COL_NATIVE_WORD),
                            cursor.getString(DBAdapter.COL_FOREIGN_WORD))
            );
        }
        cursor.close();
    }

    private void onConfirmAddButtonClick() {
        String nativeWord = nativeWordEdit.getText().toString();
        String foreignWord = foreignWordEdit.getText().toString();

        // if user did not enter a proper word pair
        if (nativeWord.isEmpty() || foreignWord.isEmpty()) {
            Toast.makeText(this, "Words cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // insert in database and update UI
        wordDB.insertRow(nativeWord, foreignWord);
        populateListViewFromDB();

        // Toast to confirm addition of word pair
        Toast.makeText(
                this,
                "Added " + "\"" + nativeWord + " - " + foreignWord + "\"",
                Toast.LENGTH_SHORT).show();

        // reset to original state
        resetFromAddState();
    }

    // reset to original state from Add mode
    private void onCancelAddButtonClick() {
        resetFromAddState();
    }

    private void onConfirmRemoveButtonClick() {
        // if no item was selected
        if (selectedRemoveItemId == -1) {
            Toast.makeText(this, "Please select a word pair to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        // delete item from DB and update UI
        wordDB.deleteRow(selectedRemoveItemId);
        populateListViewFromDB();

        // Toast to confirm removal
        Toast.makeText(this, "Word pair removed", Toast.LENGTH_SHORT).show();

        // reset to original state
        resetFromRemoveState();
    }

    // reset to original state from Remove mode
    private void onCancelRemoveButtonClick() {
        resetFromRemoveState();
    }

    // remove all word pairs
    private void onRemoveAllButtonClick() {
        wordDB.deleteAll();
        populateListViewFromDB();
        resetFromRemoveState();
    }

    // make certain fields visible and others gone
    // close keyboard
    private void resetFromAddState() {
        modeText.setText(R.string.word_list);
        nativeWordEdit.setText("");
        foreignWordEdit.setText("");

        nativeWordEdit.setVisibility(View.GONE);
        foreignWordEdit.setVisibility(View.GONE);
        confirmAddBtn.setVisibility(View.GONE);
        cancelAddBtn.setVisibility(View.GONE);

        addBtn.setVisibility(View.VISIBLE);
        removeBtn.setVisibility(View.VISIBLE);
        closeKeyboard();
    }

    // make certain fields visible and others gone
    // reset selected item from list
    // disable onClick action in list
    private void resetFromRemoveState() {
        modeText.setText(R.string.word_list);
        confirmRemoveBtn.setVisibility(View.GONE);
        cancelRemoveBtn.setVisibility(View.GONE);
        removeInfo.setText(R.string.remove_instruction);
        removeInfo.setVisibility(View.GONE);
        removeAllBtn.setVisibility(View.GONE);

        addBtn.setVisibility(View.VISIBLE);
        removeBtn.setVisibility(View.VISIBLE);

        // reset selected item
        selectedRemoveItemId = -1;

        // disable onClick behaviors
        wordList.setOnItemClickListener((parent, viewClicked, position, idDB) -> {
        });
    }

    // display word list from DB to UI
    private void populateListViewFromDB() {
        Cursor cursor = wordDB.getAllRows();

        // Allow activity to manage lifetime of the cursor.
        // DEPRECATED! Runs on the UI thread, OK for small/short queries.
        startManagingCursor(cursor);

        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_NATIVE_WORD, DBAdapter.KEY_FOREIGN_WORD};
        int[] toViewIDs = new int[]
                {R.id.native_word,     R.id.foreign_word};

        // Create adapter to may columns of the DB onto elements in the UI.
        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,		// Context
                        R.layout.word_list_item,	// Row layout template
                        cursor,					// cursor (set of DB records to map)
                        fromFieldNames,			// DB Column names
                        toViewIDs				// View IDs to put information in
                );

        // Set the adapter for the list view
        wordList.setAdapter(myCursorAdapter);
    }

    // close keyboard
    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // make an Intent to launch this activity
    public static Intent makeIntent(Context context) {
        return new Intent(context, WordListActivity.class);
    }
}