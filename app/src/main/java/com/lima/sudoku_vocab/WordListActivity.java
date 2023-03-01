package com.lima.sudoku_vocab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class WordListActivity extends AppCompatActivity {

    DBAdapter wordDB;
    Button addBtn;
    Button removeBtn;
    Button confirmAddBtn;
    EditText nativeWordEdit;
    EditText foreignWordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        openDB();
        populateListViewFromDB();
        configureViews();
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
        addBtn = findViewById(R.id.addBtn);
        removeBtn = findViewById(R.id.removeBtn);
        confirmAddBtn = findViewById(R.id.confirmAddBtn);
        nativeWordEdit = findViewById(R.id.editNativeWord);
        foreignWordEdit = findViewById(R.id.editForeignWord);

        addBtn.setOnClickListener(
                v -> onAddButtonClick()
        );

        confirmAddBtn.setOnClickListener(
                v -> onConfirmAddButtonClick()
        );
    }

    private void onAddButtonClick() {
        nativeWordEdit.setVisibility(View.VISIBLE);
        foreignWordEdit.setVisibility(View.VISIBLE);
        confirmAddBtn.setVisibility(View.VISIBLE);

        addBtn.setVisibility(View.GONE);
        removeBtn.setVisibility(View.GONE);
    }

    private void onConfirmAddButtonClick() {
        String nativeWord = nativeWordEdit.getText().toString();
        String foreignWord = foreignWordEdit.getText().toString();
        if (nativeWord.isEmpty() || foreignWord.isEmpty()) {
            Toast.makeText(this, "Words cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        nativeWordEdit.setText("");
        foreignWordEdit.setText("");
        wordDB.insertRow(nativeWord, foreignWord);
        populateListViewFromDB();

        nativeWordEdit.setVisibility(View.GONE);
        foreignWordEdit.setVisibility(View.GONE);
        confirmAddBtn.setVisibility(View.GONE);

        addBtn.setVisibility(View.VISIBLE);
        removeBtn.setVisibility(View.VISIBLE);
    }

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
        ListView myList = findViewById(R.id.word_list_view);
        myList.setAdapter(myCursorAdapter);
    }


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, WordListActivity.class);
        return intent;
    }
}