package com.example.tomek.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 *  Fragment showing photos in RecyclerView
 *
 *
 */

public class PicsFragment extends Fragment {

    // creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        RecyclerView recycler=(RecyclerView)inflater.inflate(R.layout.photos_list,viewGroup,false);

        //setting the adapter
        recycler.setAdapter(new RecycleAdapterImage());

        //setting the layout manager
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recycler.setLayoutManager(gridLayoutManager);


        return recycler;
    }

}
