package com.example.tomek.gallery;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

// TODO make this fragment's layout (XML) pretty : )
//TODO make use on Bundle...
/**
 *
 *
 * Fragment to choose pics to stroe to DB
 *
 */

public class PicsChooserFrag extends Fragment {

    private boolean keyboard_on;

    // creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        RelativeLayout relative=(RelativeLayout)inflater.inflate(R.layout.image_chooser,viewGroup,false);
        return relative;
    }

    public PicsChooserFrag(){
        super();
        keyboard_on=false;
    }

    //onStop - when user doesn't see the fragment any more
    //onREsume - when user see the fragment  once more

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
    }




}
