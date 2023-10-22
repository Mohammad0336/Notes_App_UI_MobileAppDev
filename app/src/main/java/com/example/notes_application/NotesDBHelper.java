package com.example.notes_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 6;

    // Define the table and column names
    public static final String TABLE_NAME = "notes"; // table for notes
    public static final String COLUMN_ID = "id"; // primary key
    public static final String COLUMN_TITLE = "title"; // note title
    public static final String COLUMN_DESCRIPTION = "description"; // note description
    public static final String COLUMN_COLOR = "color"; // layout colour or bg of note


    // Create the table SQL statement
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_COLOR + " INTEGER);";

    public NotesDBHelper(Context context) { // declaration of notes db
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // initialization of notes db
        db.execSQL(TABLE_CREATE);
    }

    @Override // on change of database version drop previous table
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

