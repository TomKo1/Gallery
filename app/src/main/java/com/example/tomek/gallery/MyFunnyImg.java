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
//    private Activity activity;

    public MyFunnyImg(String imgPath, String name, String description) {
        this.imgPath = imgPath;
        this.name = name;
        this.description = description;
        //this.activity=activity;
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


    //  method for loading appriopirate image as Bitmap
    public Bitmap loadBitmap(Activity activity){

        //String root= Environment.getExternalStorageDirectory().toString();


        //TODO change this strange structure
        String root=null;
        try {
             root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while loeadin BitMap");
        }




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
