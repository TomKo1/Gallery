package com.example.tomek.gallery;

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

import com.example.tomek.gallery.database.DatabaseDescription;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by tomek on 10.02.2018.
 */

// Params, Progress, Result
public class ImageSaver extends AsyncTask<Object,Void,Boolean> {

    public final String DIR_NAME_="/saved_images";


    // Result doInBackground(Params... params);

    @Override
    protected Boolean doInBackground(Object... voids) {
        ImageView preview=null;
        Activity activity=null;
        RelativeLayout relative=null;
        if(voids[0] instanceof ImageView){
            preview=(ImageView)voids[0];
        }

        if(voids[1] instanceof Activity){
            activity=(Activity)voids[1];
        }

        if(voids[2] instanceof RelativeLayout){
            relative=(RelativeLayout)voids[2];
        }

        if( activity==null || preview == null || relative == null) return null;


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
            return null;
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
            Toast.makeText(activity, "Error during saving - image not saved", Toast.LENGTH_SHORT).show();
            return null;
        }

        saveImgInfoToDb(root, fileNam,activity, relative);
        return true;
    }

    private void saveImgInfoToDb(String root, String fileNam, Activity activity,RelativeLayout relative){
        // we create URI referrimng to whole table
        Uri uri= DatabaseDescription.Picture.CONTENT_URI;

        EditText etName,etDescription;

        etDescription=(EditText)relative.findViewById(R.id.image_description);
        etName=(EditText)relative.findViewById(R.id.image_name);

        ContentResolver contentResolver=activity.getContentResolver();



        String wholePath=root+"/"+fileNam;

        ContentValues values=new ContentValues();

        //name - inserted by user in TextView
        values.put(DatabaseDescription.Picture.COLUMN_PIC_NAME,etName.getText().toString());
        //file Name...
        values.put(DatabaseDescription.Picture.COLUMN_FNAME,fileNam);
        //path -> wholePath to read image from file system
        values.put(DatabaseDescription.Picture.COLUMN_PIC_PATH,wholePath);
        //description - inserted by user in TextView
        values.put(DatabaseDescription.Picture.COLUMN_DESCRIPTION,etDescription.getText().toString());


        contentResolver.insert(uri,values);
        //TODO here update the view responsible for  showing all views
        ViewUtils.showToast(activity,"Image saved");
    }




    //void onPostExecute(Result result)
    @Override
    public void onPostExecute(Boolean result){
        if(result){

        }
    }
}
