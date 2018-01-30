package com.example.tomek.gallery;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
//????????????????????????????????????????
    public PicsChooserFrag(){
        super();
        keyboard_on=false;
    }

    //onStop - when user doesn't see the fragment any more
    //onREsume - when user see the fragment  once more

    @Override
    public void onPause(){
        super.onPause();

        //create static class ;) with this code
        //InputMethodManager is the central point of the system that manages interaction
        //between all other parts
        Activity activity=getActivity();
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //find the currently focused view, so we can grab the correct window token from it
        View view=activity.getCurrentFocus();

        //if there is none view focused it returns null
        if(view==null){
            view=new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);


    }

    @Override
    public void onResume(){
        super.onResume();
    }




}
