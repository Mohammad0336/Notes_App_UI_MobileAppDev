package com.example.notes_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Note> allNotes;
    ListView lvNotes;
    SearchView svSearchNote;
    FloatingActionButton addNote;

    NotesDBHelper dataBaseHelper = new NotesDBHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNotes = findViewById(R.id.listView);
        svSearchNote = findViewById(R.id.SearchBar);
        addNote = findViewById(R.id.floatingActionButton);

        try {
            allNotes = dataBaseHelper.getAllNotes();
            NoteAdapter adapter = new NoteAdapter(this, allNotes, new NoteAdapter.OnNoteClickListener() {

                @Override
                public void onNoteClick(Note note) {
                    Intent intent = new Intent(MainActivity.this, SecondFragment.class);
                    intent.putExtra("note_id", note.getId());
                    intent.putExtra("note_title", note.getTitle());
                    intent.putExtra("note_description", note.getDescription());
                    intent.putExtra("note_color", note.getColor());
                    intent.putExtra("note_image", note.getImage());

                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(Note note) {
                    int noteId = note.getId();
                    dataBaseHelper.deleteNote(noteId);
                    refreshNoteList();
                }
            });
            lvNotes.setAdapter(adapter);

        } catch(Exception e) {
            Toast.makeText(MainActivity.this, "EXCEPTION!", Toast.LENGTH_SHORT).show();
        }
        // Query listener to search for notes
        svSearchNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText, new NoteAdapter.OnNoteClickListener() {
                    @Override
                    public void onNoteClick(Note note) {
                        Intent intent = new Intent(MainActivity.this, SecondFragment.class);
                        intent.putExtra("note_id", note.getId());
                        intent.putExtra("note_title", note.getTitle());
                        intent.putExtra("note_description", note.getDescription());
                        intent.putExtra("note_color", note.getColor());
                        intent.putExtra("note_image", note.getImage());

                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(Note note) {
                        int noteId = note.getId();
                        dataBaseHelper.deleteNote(noteId);
                        refreshNoteList();
                    }
                });
                return false;
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstFragment.class);
                startActivity(intent);
            }
        });
    }

    public void filterNotes(String query, NoteAdapter.OnNoteClickListener onNoteClickListener) {
        List<Note> filteredNotes = new ArrayList<>();

        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        // Update the ListView with the filtered list of notes
        NoteAdapter adapter = new NoteAdapter(this, filteredNotes, onNoteClickListener);
        lvNotes.setAdapter(adapter);
    }

    private void refreshNoteList() {
        allNotes = dataBaseHelper.getAllNotes();
        NoteAdapter adapter = new NoteAdapter(this, allNotes, new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, SecondFragment.class);
                intent.putExtra("note_id", note.getId());
                intent.putExtra("note_title", note.getTitle());
                intent.putExtra("note_description", note.getDescription());
                intent.putExtra("note_color", note.getColor());
                intent.putExtra("note_image", note.getImage());

                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Note note) {
                int noteId = note.getId();
                dataBaseHelper.deleteNote(noteId);
                refreshNoteList();
            }
        });
        lvNotes.setAdapter(adapter);
    }
}
