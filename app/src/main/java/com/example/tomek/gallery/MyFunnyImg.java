package com.example.tomek.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 *      this class describes one photo with name and description to show in RecyclerView
 *x
 *
 */

public class MyFunnyImg {

    private String imgPath;
    private String name;
    private String description;


    public MyFunnyImg(String imgPath, String name, String description) {
        this.imgPath = imgPath;
        this.name = name;
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
    public String getName(){
        return name;
    }
    public String getPath(){
        return imgPath;
    }

/*
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

        if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        Log.i("Info","Asking for permission to write if not granted yet");
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4200);
    }else {

//        askForWriteStoragePer();

        try {
            //fileSav.createNewFile();            //  ???????!!!! do I need it?
            out = new FileOutputStream(fileSav);
            //saving compressed file ot the dir
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error", "Error during saving to SD");
            Toast.makeText(getActivity(), "Error during saving.", Toast.LENGTH_SHORT).show();
        }
    }
*/

    //  method for loading appriopirate image as Bitmap
    public Bitmap loadBitmap(Activity activity){

        String root= Environment.getExternalStorageDirectory().toString();

        File dir=new File(root+"/saved_images");

        Bitmap bitmap=null;
       // Log.i("Data import: ","Importig from: "+imgPath);
        File file=new File(dir,imgPath);
        if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Info","Asking for permission to read if not granted yet");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},4200);
        }else {

            try {
                FileInputStream in = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error:", "Error while reading an image");
            }
        }


        return null;
    }




}
