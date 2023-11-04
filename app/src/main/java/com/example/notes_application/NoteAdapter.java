package com.example.notes_application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);

        void onDeleteClick(Note note);
    }

    private final OnNoteClickListener onNoteClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onNoteClickListener) {
        super(context, 0, notes);
        this.onNoteClickListener = onNoteClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_item, parent, false);
        }

        Note note = getItem(position);

        TextView tvTitle = convertView.findViewById(R.id.titleText);
        TextView tvDescription = convertView.findViewById(R.id.descriptionText);

        ImageView ivImageNote = convertView.findViewById(R.id.imageView);
        ImageButton deleteButton = convertView.findViewById(R.id.imageButton);

        tvTitle.setText(note.getTitle());
        tvDescription.setText(note.getDescription());

        ivImageNote.setImageBitmap(convertBytesToImage(note.getImage()));

        // Set color selected to note background
        convertView.setBackgroundColor(note.getColor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteClickListener.onNoteClick(note);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteClickListener.onDeleteClick(note);
            }
        });

        return convertView;
    }

    // Function to convert the byte array to an image
    private Bitmap convertBytesToImage(byte[] imageBytes) {
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }
}
