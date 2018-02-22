package com.example.tomek.gallery.threads;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.tomek.gallery.MyFunnyImg;
import com.example.tomek.gallery.RecycleAdapterImage;
import com.example.tomek.gallery.ViewUtils;
import com.example.tomek.gallery.database.DatabaseDescription;

import java.io.File;

/**
 *  class representing task (thread) for deleting pictures
 */


// Params, Progress, Result
public class ImageDeleteThread extends AsyncTask<Object,Void,Boolean> {

    // TODO I think this is not good idea
    private Activity activity;
    private RecycleAdapterImage recyclerViewAdapter;

    public ImageDeleteThread(Activity activity){
        this.activity=activity;
        recyclerViewAdapter = null;
    }


    // Result doInBackground(Params... params);
    @Override
    protected Boolean doInBackground(Object... params) {
       MyFunnyImg temporaryFunnyImg = null;


        if(params[0] instanceof MyFunnyImg){
            temporaryFunnyImg=(MyFunnyImg)params[0];
        }

        if(params[1] instanceof RecycleAdapterImage){
            recyclerViewAdapter = (RecycleAdapterImage)params[1];
        }

        if(activity == null || temporaryFunnyImg == null || recyclerViewAdapter == null) return false;



        boolean resultFileRemove=removeImgFile(temporaryFunnyImg.getFileName(),activity);
       // Toast.makeText(activity,"_id: "+temporaryFunnyImg.getId(),Toast.LENGTH_SHORT).show();
        boolean resultDatabaseRemove=deleteFromDatabase(temporaryFunnyImg.getId(),activity);
       // argsToShow.remove(position);
        recyclerViewAdapter.removeFromArgsList(temporaryFunnyImg);


       boolean allExitValues= (resultDatabaseRemove && resultDatabaseRemove) ? true : false;

       return allExitValues;
    }


    private boolean removeImgFile(String fName, Activity activity){


        //TODO change this strange construction
        String root=null;
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while deleting BitMap");
            return false;
        }




        File dir=new File(root+"/saved_images");

        File file=new File(dir,fName);

        //ViewUtils.showToast(activity,"File exists?: "+file.exists());

        boolean deletingRes=file.delete();

        if(deletingRes) return true;
            else {
                return false;
         }

    }






    private boolean deleteFromDatabase(int position,Activity activity){

        // we create URI referrimng to whole table
        Uri uri= DatabaseDescription.Picture.CONTENT_URI;

        String id=String.valueOf(position);

        uri=uri.buildUpon().appendPath(id).build();

        ContentResolver contentResolver=activity.getContentResolver();

        Log.i("deleteFromDatabase: ","Uri: "+uri);

        int count= contentResolver.delete(uri,null,null);
      if(count!=1)return false;
        else{
            return true;
        }
    }



    //void onPostExecute(Result result)
    //on postExecute is executed in main thread - we can show a toast
    @Override
    public void onPostExecute(Boolean result){
        if(result){
            ViewUtils.showToast(activity,"Image deleted");
            recyclerViewAdapter.notifyDataSetChanged(); // we are sure that recyclerViewAdapter != null
        }else{
            ViewUtils.showToast(activity,"Delete image error");
        }
    }
}
