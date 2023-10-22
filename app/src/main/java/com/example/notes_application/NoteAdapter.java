package com.example.notes_application;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private List<Note> filteredNotes;

    public NoteAdapter(List<Note> notes) {
        this.noteList = notes;
        this.filteredNotes = new ArrayList<>(notes); // Initialize filteredNotes with all notes
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // container for notes view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = filteredNotes.get(position); // Use filteredNotes to display

        holder.titleText.setText(note.getTitle());
        holder.descriptionText.setText(note.getDescription());

        // Initialize the GradientDrawable
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(note.getColor());

        // Set the background of the noteItemLayout
        holder.noteItemLayout.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size(); // Return the count of filtered notes
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        // layout to put information in when creating a note
        public TextView titleText;
        public TextView descriptionText;
        public LinearLayout noteItemLayout;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            noteItemLayout = itemView.findViewById(R.id.noteItemLayout);
        }
    }

    public void addNewNote(Note note) { // submission of note to db
        noteList.add(note);
        notifyDataSetChanged();
    }

    // Filter notes based on user input
    public void filterNotes(String query) { // when input is in the search view if note title contains substring show specific view
        filteredNotes.clear();
        if (query.isEmpty()) {
            filteredNotes.addAll(noteList); // If the query is empty, show all notes
        } else {
            query = query.toLowerCase();
            for (Note note : noteList) {
                if (note.getTitle().toLowerCase().contains(query) || note.getDescription().toLowerCase().contains(query)) {
                    filteredNotes.add(note);
                }
            }
        }
        notifyDataSetChanged(); // Notify the adapter of the dataset change
    }
}
