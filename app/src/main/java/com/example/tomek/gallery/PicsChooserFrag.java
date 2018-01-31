package com.example.tomek.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tomek.gallery.database.GalleryDataBaseContentProvider;

import java.io.IOException;

// TODO make this fragment's layout (XML) pretty : )
//TODO make use on Bundle...
/**
 *
 *
 * Fragment to choose pics to stroe to DB
 *
 */

public class PicsChooserFrag extends Fragment {


    //TODO imageVIewTOByte to stroe a pic in the database
    //TODO add photo making and basic editing (with system editor)
    //reference to the button to choose & save
    private Button btnChoose,btnSave;
    //preview of the choosen pic
    private ImageView preview;

    //reference to ContentProvider
    private GalleryDataBaseContentProvider contentProvider;

    // creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        RelativeLayout relative=(RelativeLayout)inflater.inflate(R.layout.image_chooser,viewGroup,false);

        // we init. reference to appropriate view
        preview=(ImageView)relative.findViewById(R.id.pic_preview);

        //ContentProvider reference
        contentProvider=new GalleryDataBaseContentProvider();

        // we gain references to appriopriate buttons and add them onclicklisteners
        btnChoose=(Button)relative.findViewById(R.id.chose_pic);
        btnSave=(Button)relative.findViewById(R.id.save_pic);

        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //calling private method which makes intent to find some pics in system
                //and preview them in ImageView
                findPicInSystem();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                findPicInSystem();

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                savePicToDb();

            }
        });




        return relative;
    }
//????????????????????????????????????????


    private void savePicToDb(){
        Toast.makeText(getActivity(),"Saving...",Toast.LENGTH_SHORT).show();
    }


    private void findPicInSystem(){
        Toast.makeText(getActivity(),"Finding picture in gallery...",Toast.LENGTH_SHORT).show();
        //we check if we have the permission
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //we don't have permission - try to gain it
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);

        }else {

            makeGalleryIntent();
        }
    }


    //TODO understand this code
    private void makeGalleryIntent(){

        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);

        galleryIntent.setType("image/*");

        if(galleryIntent.resolveActivity(getActivity().getPackageManager())!=null) startActivityForResult(galleryIntent,1000);

    }


    //TODO understand this code
    // https://developer.android.com/training/basics/intents/result.html#ReceiveResult  <-- To read
    // https://developer.android.com/reference/java/net/URI.html <-- To read
    //handles the rsults of intent
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

        Activity activity=getActivity();

        if(requestCode==1000 && resultCode==activity.RESULT_OK){

            //returns the Uri of the data this intent is targeting or null
            Uri returnUri=data.getData();
            if(returnUri==null){
                Toast.makeText(getActivity(),"Some error occured",Toast.LENGTH_SHORT).show();
                return ;
            }
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);

                preview.setImageBitmap(image);


            }catch(IOException e){
                e.printStackTrace();
                Toast.makeText(activity, "Some error occured", Toast.LENGTH_SHORT).show();
                return ;
            }

        }else if(resultCode!=activity.RESULT_OK){
            Toast.makeText(activity,"Some error", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity,"Sth else", Toast.LENGTH_SHORT).show();
        }
    }


}
