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
import java.io.Serializable;
import java.util.StringTokenizer;

/**
 *
 *      this class describes one photo with name and description to show in RecyclerView
 *x
 *
 */

public class MyFunnyImg implements Serializable{

    private String imgPath;
    private String name;
    private String description;
    private String fileName;
    private Integer _id;
    //    private Activity activity;

    public Integer getId() {
        return _id;
    }



    public MyFunnyImg(String imgPath, String name, String description,String fileName,Integer _id) {
        this.imgPath = imgPath;
        this.name = name;
        this.description = description;
        this.fileName=fileName;
        this._id=_id;
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
    public String getFileName(){return fileName;}







}
