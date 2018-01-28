package com.example.tomek.gallery.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.tomek.gallery.R;

/**
 *
 * ContentProvider encapsulates data and provide it to
 * applications through single ContentResolver interface (...)
 * a ContentProvider is only required if you need to
 * share data between multiple applications
 *
 */

// TO:DO - do static import to make the code more clear ;)

public class GalleryDataBaseContentProvider extends ContentProvider {

    // in order to get access to the db
    private GalleryDatabaseHelper dbHelper;
    private String errorMessage;

    // UriMatcher - usefulclass when writting ContentProvider - it helps to
    //recognize requests
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    // consts used to recognize the operation
    private static final int ONE_PIC=1;//operation concerning only 1 pic
    private static final int WHOLE_TABLE=2;//operation on the whole table

    // static block configurating UriMAtcher object
    //static block is being called only one when the class is initialized/loaded
    static{
        // addUri adds a Uri to match and the code to return  when this UTI is matched

        //generally we build up the tree of queries
        // Uri address of only one pic - tablename/number
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,DatabaseDescription.Picture.TABLE_NAME_+"/#",ONE_PIC);
        //Uri address of the whole table -  tablename
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,DatabaseDescription.Picture.TABLE_NAME_,WHOLE_TABLE);
    }


    @Override
    public boolean onCreate() {
        dbHelper=new GalleryDatabaseHelper(getContext());
        errorMessage=getContext().getString(R.string.unsupportedQuery);
        return true;
    }


    // this method is not required in this app - return null
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    /*
    *
    * methods below are used for database managment
    *
     */


    // TO:DO think about which var can be null's : )
    // TO:DO add option of sharing meme
    // TO:DO add recyclerview displaying all pics
    // TO:DO add some sort of 'likes'
    // TO:DO make static import

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //SQLiteQueryBuilder helps us to build a query
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();
        // this builder is concerning our pics table
        queryBuilder.setTables(DatabaseDescription.Picture.TABLE_NAME_);

        // we try to match the uri
        switch(uriMatcher.match(uri)){
            case ONE_PIC:
                // SQL with WHERE clause (where id=id_from_passed_uri)
                queryBuilder.appendWhere(DatabaseDescription.Picture.TABLE_NAME_+"="+uri.getLastPathSegment());

                break;
            case WHOLE_TABLE: // we chose whole table...

                break;
            default:
                unsupportedOperation();
        }


        // we execute the query
        //Curosr provides random read-write access to the result set returned by database query
        Cursor cursor=queryBuilder.query(dbHelper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);

        //ContentResolver provides applications accesss to the content model
        //the method benath registers to watch a conntent Uri changes
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }


    private void unsupportedOperation(){
        // informing with helpful Toast
        Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
        // we indicate unsupportedoperation by throwing appropriate exception
        throw new UnsupportedOperationException(errorMessage);

    }


    //save new Pic to database
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri newPicUri=null;

        switch(uriMatcher.match(uri)){
            case WHOLE_TABLE:

                // insert new pic to database
                long insRowId=dbHelper.getWritableDatabase().insert(DatabaseDescription.Picture.TABLE_NAME_,null,contentValues);

                // create Uri address if success
                //else throw an exception
                if(insRowId>0){ // SQLite rows starts at 1
                    // we create new Pic Uri...
                    newPicUri=DatabaseDescription.Picture.createUri(insRowId);


                     // notify listeners that database was modified
                    getContext().getContentResolver().notifyChange(uri,null);
                }else{
                    throw new SQLException(getContext().getString(R.string.insert_failure));
                }

                break;
            default:
                unsupportedOperation();
        }


        return  newPicUri;


    }

    // TO:DO implement delete method !!!
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    //update previously saved pic
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        int numbersOfRowsUpdated=0; // to check if the update was successful

        switch(uriMatcher.match(uri)){
            case ONE_PIC:
                    numbersOfRowsUpdated=dbHelper.getWritableDatabase().update(DatabaseDescription.Picture.TABLE_NAME_,contentValues,DatabaseDescription.Picture._ID+"="+uri.getLastPathSegment(),strings);
                break;
            default:
                unsupportedOperation();
        }


        // if sth was updated we should notifychange
        if(numbersOfRowsUpdated>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numbersOfRowsUpdated;
    }
}
