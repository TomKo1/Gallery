package com.example.tomek.gallery.fragments;

import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tomek.gallery.MyFunnyImg;
import com.example.tomek.gallery.R;
import com.example.tomek.gallery.ViewUtils;

import java.io.File;

/**
 *  Class describing fragment showing details of a clicked image
 */

public class ImageDetail extends Fragment {


    private RelativeLayout wholeLayout;
    private TextView txtName;
    private ImageView imgDetails;

    private MyFunnyImg funnyImg;

//TODO make Toolbar invisible
    //TODO thread to delete

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        wholeLayout=(RelativeLayout) inflater.inflate(R.layout.image_details,viewGroup,false);

        if(getArguments()!=null)
            funnyImg = (MyFunnyImg) getArguments().get(ViewUtils.BUNDLE_DETAILS_KEY);

        txtName=(TextView)wholeLayout.findViewById(R.id.txtDetails);
        initTextView();
        imgDetails=(ImageView)wholeLayout.findViewById(R.id.img_details);
        initImgView();


        //hide ActionBar
        manageActionBar(true);



        return wholeLayout;
    }

    private void manageActionBar(boolean hide){
        //hide ActionBar
        if(getActivity() instanceof AppCompatActivity){
            ActionBar actionBar=(ActionBar)((AppCompatActivity)getActivity()).getSupportActionBar();

            if(hide){
                actionBar.hide();
            }else{
                actionBar.show();
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        manageActionBar(false);
    }

    // got practices of instantiate fragments
    // https://gunhansancar.com/best-practice-to-instantiate-fragments-with-arguments-in-android/

    private void initImgView(){
        String root=null;
        try {
            root = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while loeadin BitMap");
        }

        File dir=new File(root+"/saved_images");
        File file=new File(dir,funnyImg.getFileName());
        Glide.with(getActivity()).load(file).centerCrop().into(imgDetails);

        imgDetails.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                int value=(txtName.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
                txtName.setVisibility(value);
            }
        });
    }


    private void initTextView(){
        txtName.setVisibility(View.VISIBLE);
        txtName.setText(funnyImg.getName());
    }


}
