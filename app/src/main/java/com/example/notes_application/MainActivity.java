package com.example.notes_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // Initializing the recycler view
    private NoteAdapter noteAdapter; // Initializing the NoteAdapter obj
    private NotesDataSource dataSource; // Initializing the NotesDataSource obj
    private SearchView searchView; // Initializing the SearchView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recycler view for the specific notes
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataSource = new NotesDataSource(this);
        dataSource.open();

        List<Note> notes = getNotesFromDatabase();
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);

        // fab to take you to new note screen
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FirstFragment.class);
            startActivity(intent);
        });

        // search bar identification and input parser
        searchView = findViewById(R.id.SearchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.filterNotes(newText);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() { // destroy method
        super.onDestroy();
        dataSource.close();
    }

    // Display notes on main page on recycle view
    private List<Note> getNotesFromDatabase() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = dataSource.getAllNotes();

        if (cursor != null) {
            Log.d("CursorCount", "Number of rows in cursor: " + cursor.getCount());

            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("ColumnName", columnName);
            }

            // while loop to print all db values on xml in recycleview
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(NotesDBHelper.COLUMN_TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(NotesDBHelper.COLUMN_DESCRIPTION));
                @SuppressLint("Range") int color = cursor.getInt(cursor.getColumnIndex(NotesDBHelper.COLUMN_COLOR));

                notes.add(new Note(title, description, color));
            }
            cursor.close();
        }
        return notes; // print the notes from db
    }
}
