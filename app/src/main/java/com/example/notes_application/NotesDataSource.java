package com.example.notes_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotesDataSource {
    private SQLiteDatabase database;
    private NotesDBHelper dbHelper;

    public NotesDataSource(Context context) {
        dbHelper = new NotesDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertNote(String title, String description, int color) {
        ContentValues values = new ContentValues();
        values.put(NotesDBHelper.COLUMN_TITLE, title);
        values.put(NotesDBHelper.COLUMN_DESCRIPTION, description);
        values.put(NotesDBHelper.COLUMN_COLOR, color);

        long insertId = database.insert(NotesDBHelper.TABLE_NAME, null, values);

        return insertId;
    }

    public Cursor getAllNotes() {
        return database.query(NotesDBHelper.TABLE_NAME, null, null, null, null, null, null);
    }
}
