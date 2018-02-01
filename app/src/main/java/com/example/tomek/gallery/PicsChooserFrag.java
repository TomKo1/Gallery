package com.example.tomek.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tomek.gallery.database.GalleryDataBaseContentProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

// TODO make this fragment's layout (XML) pretty : )
//TODO make use on Bundle...
/**
 *
 *
 * Fragment to choose pics to stroe to DB
 *
 */

public class PicsChooserFrag extends Fragment {

    public final String DIR_NAME_="/saved_images";
    private static BigInteger FILE_COUNTER_=new BigInteger("0");
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

                savePicToSD();

            }
        });




        return relative;
    }
//????????????????????????????????????????


    // saving pics to the SD's dir
    //TODO make it so that pics doesn't repeat
    private void savePicToSD(){
         Toast.makeText(getActivity(),"Saving...",Toast.LENGTH_SHORT).show();
        //getting BitMap from ImageView
        Bitmap imageToSave=((BitmapDrawable)preview.getDrawable()).getBitmap();

        // getting env var representing path
        String root= Environment.getExternalStorageDirectory().toString();
        Toast.makeText(getActivity(),root,Toast.LENGTH_LONG).show();
        File dir=new File(root+DIR_NAME_);



         dir.mkdirs();

        String fileNam="Image-"+FILE_COUNTER_+".jpg";
        FILE_COUNTER_=FILE_COUNTER_.add(BigInteger.valueOf(1));
        File fileSav=new File(dir,fileNam);


        //I don't use try-with-resources because of API lvl
        FileOutputStream out=null;
        try{
            out=new FileOutputStream(fileSav);

            //saving compressed file ot the dir
            imageToSave.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
            out.close();
        }catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity(), "Error during saving.", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getActivity(),"Image saved.",Toast.LENGTH_LONG).show();

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

    //TODO end of code to understand




}
