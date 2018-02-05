package com.example.tomek.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;

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
    private String fileName;
//    private Activity activity;

    public MyFunnyImg(String imgPath, String name, String description,String fileName) {
        this.imgPath = imgPath;
        this.name = name;
        this.description = description;
        this.fileName=fileName;
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

        //TODO - I have to pass fileName as 2 argument in File :)
        //TODO - first pic doesn't display - strange
        //TODO - why is warning about storing (writing) display even though I am only choosing ->???
        //TODO - make another column with file name ( for example Image-0.jpg)
        /*
        StringTokenizer tokenizer=new StringTokenizer(imgPath,"/");
        String fileName=null;
        while(tokenizer.hasMoreElements()){
           fileName=tokenizer.nextToken();
        t}*/
        Log.i("Loading",fileName);
        File file=new File(dir, fileName);

        if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Info","Asking for permission to read if not granted yet");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},4200);
        }else {

            try {
                FileInputStream in = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(in);
                if(bitmap==null)Toast.makeText(activity,"In RecyclerView bitmap is null1",Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("Error:", "Error while reading an image");
            }
        }

        if(bitmap==null)Toast.makeText(activity,"In RecyclerView bitmap is null2",Toast.LENGTH_SHORT).show();
        return bitmap;
    }




}
