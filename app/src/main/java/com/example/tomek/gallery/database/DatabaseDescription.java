package com.example.tomek.gallery.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * this class provides info needed by
 * ContentProvider object
 */

public class DatabaseDescription {

    // address of ContentProvider - typically this is a pacage name
    public static final String AUTHORITY="com.example.tomek.gallery.database";

    // base URi address tto makek contact with ContentProvider object
    private static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    /*
     * nested class describing the content of database
     *
     * BaseColumns provides names for the very commin _ID and _COUNT columns
     */
    public static final class Picture implements BaseColumns{


        //name of the table
        public static final String TABLE_NAME_="AllPics";

        // Uri address to make contact with pics table
        //content://com.example.tomek.gallery.database/AllPics
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME_).build();



        // names of columns in daatbase
        public static final String COLUMN_PIC_="pic";
        public static final String COLUMN_PIC_NAME="name";

        //creates Uri adress of specific Pic
        public static Uri createUri(long id){
            //withAppendeddIF appends the given ID to the end of the path
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

}
