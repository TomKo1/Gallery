package com.example.tomek.gallery;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * single image details
 */

public class ImageDetail extends Fragment {


    private LinearLayout wholeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        wholeLayout=(LinearLayout)inflater.inflate(R.layout.image_details,viewGroup,false);





        return wholeLayout;
    }

}
