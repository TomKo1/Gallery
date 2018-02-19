package com.example.tomek.gallery;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomek.gallery.database.DatabaseDescription;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  Fragment showing photos in RecyclerView
 *
 *
 */


//TODO make fetching and storing data more efficient (threads and so on)
public class PicsFragment extends Fragment {

    private List<MyFunnyImg> funnyImagesList;
    private ContentResolver contentResolver;
    private Activity activity;

    // creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        RecyclerView recycler=(RecyclerView)inflater.inflate(R.layout.photos_list,viewGroup,false);

        funnyImagesList=new ArrayList<>();
        contentResolver=getActivity().getContentResolver();
        activity=getActivity();
        fetchDataFromDb();


        //setting the adapter
        recycler.setAdapter(new RecycleAdapterImage(funnyImagesList,activity));

        //setting the layout manager
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recycler.setLayoutManager(gridLayoutManager);



        return recycler;
    }


    //TODO make it more efficiently - maybe in a UI thread ???
    // if someone knows how to implement it more efficiently please text me
    private void fetchDataFromDb(){

        Uri uri= DatabaseDescription.Picture.CONTENT_URI;

        //query for ContentResolver
        //TODO warning: passing null as projection is unefficient - returns all columns
        //TODO there is no cancelation signal !!!
        Cursor cursor=contentResolver.query(uri,null,null,null,null,null);

        while(cursor.moveToNext()){
            //TODO maybe a method???
            populateList(cursor);
        }
    }

    private void populateList(Cursor cursor){
        String path=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Picture.COLUMN_PIC_PATH));
        String name=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Picture.COLUMN_PIC_NAME));
        String fileName=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Picture.COLUMN_FNAME));
        String description=cursor.getString(cursor.getColumnIndex(DatabaseDescription.Picture.COLUMN_DESCRIPTION));
        Integer id=cursor.getInt(cursor.getColumnIndex(DatabaseDescription.Picture._ID));

        funnyImagesList.add(new MyFunnyImg(path,name,description,fileName,id));
    }

}
