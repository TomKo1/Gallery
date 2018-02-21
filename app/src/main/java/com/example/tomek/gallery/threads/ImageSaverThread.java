package com.example.tomek.gallery.threads;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tomek.gallery.ViewUtils;
import com.example.tomek.gallery.database.DatabaseDescription;

import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * Class representing thread (task in Background) whic saved the Image
 *
 *
 */

// Params, Progress, Result
public class ImageSaverThread extends AsyncTask<Object,Void,Boolean> {

    private final String DIR_NAME_="/saved_images";

    private Activity activity; //TODO WeakReference ???
    private String name;
    private String description;

    public ImageSaverThread(Activity activity,String name, String description){
        this.activity=activity;
        this.name=name;
        this.description=description;
    }

    // Result doInBackground(Params... params);
    @Override
    protected Boolean doInBackground(Object... voids) {
        ImageView preview=null;
        RelativeLayout relative=null;

        if(voids[0] instanceof ImageView){
            preview=(ImageView)voids[0];
        }
        if(voids[1] instanceof RelativeLayout){
            relative=(RelativeLayout)voids[1];
        }

        if(preview == null || relative == null) return false;


        //getting BitMap from ImageView
        Bitmap imageToSave=((BitmapDrawable)preview.getDrawable()).getBitmap();

        //TODO change this strange structure
        String root=null;
        //this dir is not accessed by the user and is automatically
        //uninstalled after removing the app
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while savign the Pic");
            return false;
        }

        File dir=new File(root+DIR_NAME_);
        dir.mkdirs();
        String fileNam=""+System.currentTimeMillis(); //timestamp as an file name
        File fileSav=new File(dir,fileNam);

        FileOutputStream out=null;

        if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Info","Asking for permission to write if not granted yet");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4200);
        }

        try {
            out = new FileOutputStream(fileSav);
            //saving compressed file ot the dir
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("PicsChooserFrag", "Error during saving to SD");
            return false;
        }

        Log.i("Saved to: ",root);

        return  saveImgInfoToDb(root, fileNam,activity, relative);

    }

    private boolean saveImgInfoToDb(String root, String fileNam, Activity activity,RelativeLayout relative){
        // we create URI referrimng to whole table
        Uri uri= DatabaseDescription.Picture.CONTENT_URI;

        ContentResolver contentResolver=activity.getContentResolver();



        String wholePath=root+"/"+fileNam;

        ContentValues values=new ContentValues();


        values.put(DatabaseDescription.Picture.COLUMN_PIC_NAME,name);
        values.put(DatabaseDescription.Picture.COLUMN_FNAME,fileNam);
        values.put(DatabaseDescription.Picture.COLUMN_PIC_PATH,wholePath);
        values.put(DatabaseDescription.Picture.COLUMN_DESCRIPTION,description);


        contentResolver.insert(uri,values);
        Log.i("Image Saver: ","Image saved");
        return true;
    }




    //void onPostExecute(Result result)
    //on postExecute is executed in main thread - we can show a toast
    @Override
    public void onPostExecute(Boolean result){
        if(result){
            ViewUtils.showToast(activity,"Image saved");
        }else{
            ViewUtils.showToast(activity,"Saving image error");
        }
    }
}
