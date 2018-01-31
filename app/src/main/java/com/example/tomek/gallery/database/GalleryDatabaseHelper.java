package com.example.tomek.gallery.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
    this class is responsible for creating
    the database
 */


public class GalleryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="GalleryDataBase.db";
    private static final int DATABASE_VERSION=1;

    //constructor
    public GalleryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }


    //creates whole table
    //SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL creating appropriate table
        final String CREATE_TABLE_SQL_="CREATE_TABLE "+ DatabaseDescription.Picture.TABLE_NAME_+" ("+
                DatabaseDescription.Picture._ID+" integer primary key, "+
                DatabaseDescription.Picture.COLUMN_PIC_NAME+" TEXT, "+
                DatabaseDescription.Picture.COLUMN_PIC_+ " BLOB);";

        sqLiteDatabase.execSQL(CREATE_TABLE_SQL_);


    }


    //defines the way the database is upgraded - here initially empty
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
