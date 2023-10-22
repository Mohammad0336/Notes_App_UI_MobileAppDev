package com.example.notes_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class FirstFragment extends AppCompatActivity {

    // Initialize the coloured Buttons
    private Button yellowButton;
    private Button orangeButton;
    private Button blueButton;
    private LinearLayout linearLayout; // Initialize the Layout
    private NoteAdapter noteAdapter; // Initialize the NoteAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // component identification by ID
        linearLayout = findViewById(R.id.linearLayout);
        yellowButton = findViewById(R.id.yellowButton);
        orangeButton = findViewById(R.id.orangeButton);
        blueButton = findViewById(R.id.blueButton);

        // Initialize the NoteAdapter
        noteAdapter = new NoteAdapter(new ArrayList<>());
    }

    public void changeBackgroundColor(View view) { // change colour from input buttons for UI
        int newColor = 0;

        if (view == yellowButton) {
            newColor = Color.parseColor("#FFFF00");
        } else if (view == orangeButton) {
            newColor = Color.parseColor("#FFA600");
        } else if (view == blueButton) {
            newColor = Color.parseColor("#00DDDD");
        }

        view.setBackgroundColor(newColor);
        linearLayout.setBackgroundColor(newColor);
    }

    public void returnHome(View view) { // cancel button to go home from new note page
        Intent intent = new Intent(FirstFragment.this, MainActivity.class);
        startActivity(intent);
    }

    public void insertNote(View view) { // parsing information from inputs to put into db
        NotesDataSource dataSource = new NotesDataSource(this);
        dataSource.open();

        EditText title_input = findViewById(R.id.titleText); // title input
        String title = title_input.getText().toString();

        EditText description_input = findViewById(R.id.descriptionText); // description input
        String description = description_input.getText().toString();

        LinearLayout colorLayout = findViewById(R.id.linearLayout); // layout color
        int color = ((ColorDrawable) colorLayout.getBackground()).getColor();

        if (title.isEmpty()) { // Only allow notes to be saved if title has text in it
            showToast("Please enter a title for the note.");
            return;
        }

        long noteId = dataSource.insertNote(title, description, color); // format for input

        dataSource.close();

        if (noteId != -1) {
            showToast("Note added successfully");

            Note newNote = new Note(title, description, color);

            // Add the new note to the adapter
            noteAdapter.addNewNote(newNote);

            title_input.setText("");
            description_input.setText("");

            // return back to home page
            Intent intent = new Intent(FirstFragment.this, MainActivity.class);
            startActivity(intent);
        } else { // no title input or other error toast message
            showToast("Failed to add note");
        }
    }

    private void showToast(String message) { // toast message output
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override // back button to go home from new note page
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
