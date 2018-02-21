package com.example.tomek.gallery.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tomek.gallery.ImageSaverThread;
import com.example.tomek.gallery.R;
import com.example.tomek.gallery.database.DatabaseDescription;

import java.io.File;
import java.io.FileOutputStream;
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

    //TODO delete this var
    public final String DIR_NAME_="/saved_images";

    //TODO imageVIewTOByte to stroe a pic in the database
    //TODO add photo making and basic editing (with system editor)
    //reference to the button to choose & save
    private Button btnChoose,btnSave;
    //preview of the choosen pic
    private ImageView preview;

    //reference to the EditText with description and name from user
    private EditText etDescription, etName;

    //reference to image_chooser view
    private RelativeLayout relative;

    //reference to ContentProvider
    private ContentResolver contentResolver;

    // creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
         relative=(RelativeLayout)inflater.inflate(R.layout.image_chooser,viewGroup,false);

        // we init. reference to appropriate view
        preview=(ImageView)relative.findViewById(R.id.pic_preview);

        //ContentResolver reference
        contentResolver=getActivity().getContentResolver();

        // we gain references to appriopriate buttons and add them onclicklisteners
        btnChoose=(Button)relative.findViewById(R.id.chose_pic);
        btnSave=(Button)relative.findViewById(R.id.save_pic);

        etDescription=(EditText)relative.findViewById(R.id.image_description);
        etName=(EditText)relative.findViewById(R.id.image_name);

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
                String name=etName.getText().toString();
                String description=etDescription.getText().toString();
                etName.setText("");
                etDescription.setText("");
                ImageSaverThread thread=new ImageSaverThread(getActivity(),name,description);
                thread.execute(preview,relative);


            }
        });




        return relative;
    }

    // saving pics to the SD's dir
    //TODO make this method shorter
    //TODO - this can be done as a thread !!!
    private void savePicToSD(){
         Toast.makeText(getActivity(),"Saving...",Toast.LENGTH_SHORT).show();
        //getting BitMap from ImageView
        Bitmap imageToSave=((BitmapDrawable)preview.getDrawable()).getBitmap();

        //TODO change this strange structure
        String root=null;
        //this dir is not accessed by the user and is automatically
        //uninstalled after removing the app
        try {
             root = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while savign the Pic");
            return;
        }

        File dir=new File(root+DIR_NAME_);
        dir.mkdirs();
        String fileNam=""+System.currentTimeMillis(); //timestamp as an file name
        File fileSav=new File(dir,fileNam);

        FileOutputStream out=null;

        if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Info","Asking for permission to write if not granted yet");
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4200);
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
            Toast.makeText(getActivity(), "Error during saving - image not saved", Toast.LENGTH_SHORT).show();
            return ;
        }

        saveImgInfoToDb(root, fileNam);
    }


    private void saveImgInfoToDb(String root, String fileNam){
        // we create URI referrimng to whole table
        Uri uri=DatabaseDescription.Picture.CONTENT_URI;
        // we insert this data to database - we should to this as a thread???

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

        Toast.makeText(getActivity(),"Image saved."+wholePath,Toast.LENGTH_LONG).show();



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
                //TODO rescale this bitmap --?
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
