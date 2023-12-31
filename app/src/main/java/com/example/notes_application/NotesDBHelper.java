package com.example.notes_application;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotesDBHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_COLOR = "COLOR";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String NOTES_TABLE = "NOTES";

    public NotesDBHelper(@Nullable Context context) {
        super(context, "notes.db", null, 4); // Ensure the version number matches your database schema
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + NOTES_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT,  " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_COLOR + " INT, " +
                COLUMN_IMAGE + " BLOB)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema updates when the version number changes
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
            onCreate(db);
        }
    }


    public void addOne(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());
        cv.put(COLUMN_COLOR, note.getColor());
        cv.put(COLUMN_IMAGE, note.getImage());

        db.insert(NOTES_TABLE, null, cv);
    }

    public List<Note> getAllNotes() {
        List<Note> returnList = new ArrayList<>();

        // Get all notes in DB
        String queryString = "SELECT * FROM " + NOTES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {

            // Loop through cursor and create new note objects and then put them into return list
            do {
                int noteID = cursor.getInt(0);
                String noteTitle = cursor.getString(1);
                String noteDescription = cursor.getString(2);
                int noteColor = cursor.getInt(3);
                byte[] noteImage;

                //if there is no image
                if (!cursor.isNull(4)) {
                    noteImage = cursor.getBlob(4);
                } else {
                    noteImage = null;
                }

                Note newNote = new Note(noteID, noteTitle, noteDescription, noteColor, noteImage);
                returnList.add(newNote);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());
        cv.put(COLUMN_COLOR, note.getColor());
        cv.put(COLUMN_IMAGE, note.getImage());

        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(note.getId())};

        int rowsUpdated = db.update(NOTES_TABLE, cv, whereClause, whereArgs);

        db.close();
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(noteId)};
        db.delete(NOTES_TABLE, whereClause, whereArgs);
        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = "NOTES";
        db.delete(tableName, null, null);
        db.close();
    }
}