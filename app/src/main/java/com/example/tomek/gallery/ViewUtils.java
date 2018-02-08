package com.example.tomek.gallery;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 *
 *
 * Utilities for views
 *
 */


//TODO change this class so that it is written in Kotlin

public class ViewUtils {


    //TODO add here a method which changes the string on the TOOLBAR




    //method to hide SoftInput
    public static void hideKeyBoard(Activity activity){

        //InputMethodManager is the central point of the system that manages interaction
        //between all other parts
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //find the currently focused view, so we can grab the correct window token from it
        View view=activity.getCurrentFocus();

        //if there is none view focused it returns null
        if(view==null){
            view=new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

    }
}
