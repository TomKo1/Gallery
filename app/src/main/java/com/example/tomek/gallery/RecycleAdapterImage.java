package com.example.tomek.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tomek.gallery.database.DatabaseDescription;

import java.io.File;
import java.util.List;

/**
 *
 *  SIMPLE ADAPTER TO BIND IMAGES WITH RECYCLERVIEW
 *
 *
 */

public class RecycleAdapterImage extends RecyclerView.Adapter<RecycleAdapterImage.ViewHolder>{

    private int[]imageIDs;

    private List<MyFunnyImg> argsToShow;

    private Activity activity;






    //called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item  --- !!!! SQLite !!!!!!
    @Override
    public RecycleAdapterImage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater creates a layout XML file into its corresponding View object
        //static from method obtains the LayoutInflater from the given context
        CardView newCardView=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_card,parent,false);

        return new ViewHolder(newCardView);
    }


    //TODO we need to pass here all names, desriptions, and Bitmaps from DB
    // constructor - to change !!!!!!!!!!!!!!!!!!!!!
    public RecycleAdapterImage(List<MyFunnyImg>argsToShow, Activity activity){
        this.imageIDs=new int[10]; // fake 10 images...


        this.activity=activity;
        this.argsToShow=argsToShow;
    }



    //called by REcyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(RecycleAdapterImage.ViewHolder holder,int position) {
        //getting information about this specific image
        CardView cardView=holder.cardView;
        final ImageView imageView=holder.picture;
        ImageView moreDots=holder.moreDots;

        MyFunnyImg toShow=argsToShow.get(position);


        //Bitmap image=argsToShow.get(position).loadBitmap(activity);
        loadBitmap(imageView,toShow);

        String description=toShow.getDescription();
        //String name=toShow.getName();


        //TODO this may be the same as position
        final int position1=position;

        moreDots.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showPopUpMenu(view, imageView, position1);
            }
        });



        //imageView.setImageBitmap(image);
        TextView textView=(TextView)cardView.findViewById(R.id.card_view_des);
        //textView.setText(description);//description
        textView.setText(argsToShow.get(position).getFileName());
        TextView textView2=(TextView)cardView.findViewById(R.id.card_view_text1);
        textView2.setText(argsToShow.get(position).getDescription());
    }




    //TODO is this final int "nice"???
    //method shows PopUpMenu
    private void showPopUpMenu(View view, final ImageView imageView,final int position){

        PopupMenu popup=new PopupMenu(activity,view);

        MenuInflater inflater=popup.getMenuInflater();

        inflater.inflate(R.menu.single_pic_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return onSingleMenuItemClick(item, imageView, position);
            }
        });
        popup.show();
    }

private boolean onSingleMenuItemClick(MenuItem item,ImageView imageView,int position){
        int id=item.getItemId();
        switch (id){
            case R.id.share:
                ViewUtils.showToast(activity,"Sharing");
                Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                shareImgToSocMedia(bitmap);
                break;
            case R.id.more_info:
                ViewUtils.showToast(activity,"Showing more info");
                Bitmap bitmap2=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                showMoreInfo(bitmap2);
                break;
            case R.id.delete_img:
                deleteImg(position);
                break;
            default:
                ViewUtils.showToast(activity,"No such option");
        }
        return true;
}
//TODO if this method is working good?
    private void deleteImg(int position){

        MyFunnyImg temporaryImg=argsToShow.get(position);
        removeImgFile(temporaryImg.getFileName());
        Toast.makeText(activity,"_id: "+temporaryImg.getId(),Toast.LENGTH_SHORT).show();
        deleteFromDatabase(temporaryImg.getId());
        //deleteFromDatabase(position);
        argsToShow.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
        Toast.makeText(activity,"Ilosc ViewHolderow "+argsToShow.size(),Toast.LENGTH_SHORT).show();
        notifyItemRangeChanged(position,argsToShow.size());
    }


    private void removeImgFile(String fName){


            //TODO change this strange construction
        String root=null;
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while deleting BitMap");
        }




        File dir=new File(root+"/saved_images");

        File file=new File(dir,fName);

        //ViewUtils.showToast(activity,"File exists?: "+file.exists());

        boolean deletingRes=file.delete();
        //ViewUtils.showToast(activity,"Image deleting result: "+deletingRes);
        //ViewUtils.showToast(activity,"File exists?: "+file.exists());

    }

    private void deleteFromDatabase(int position){

        // we create URI referrimng to whole table
        Uri uri= DatabaseDescription.Picture.CONTENT_URI;

        String id=String.valueOf(position);

        uri=uri.buildUpon().appendPath(id).build();

        ContentResolver contentResolver=activity.getContentResolver();

        Log.i("deleteFromDatabase: ","Uri: "+uri);

       int count= contentResolver.delete(uri,null,null);
       ViewUtils.showToast(activity,"Deleted: "+count+" rows");
    }





    private void showMoreInfo(Bitmap bitmap){
          ImageDetail nextFrag=new ImageDetail();
          activity.getFragmentManager().beginTransaction().
                  replace(R.id.main_fragment,nextFrag,"Details frag").
                  addToBackStack(null).
                  setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                  commit();


    }


    private void shareImgToSocMedia(Bitmap bitmap){
        String path= MediaStore.Images.Media.insertImage(activity.getContentResolver(),bitmap,"Image","From Gallery");
        Uri uri=Uri.parse(path);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "I found something cool!");
        activity.startActivity(Intent.createChooser(share, "Share Your Design!"));
    }


    // loads BitMap into ImageView using glide
    private void loadBitmap(ImageView imageView,MyFunnyImg toShow){


        //TODO change this strange construction
        String root=null;
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while loeadin BitMap");
        }




        File dir=new File(root+"/saved_images");
        Log.e("FileName:",toShow.getFileName());
        File file=new File(dir,toShow.getFileName());
        Glide.with(activity).load(file).into(imageView);


    }






    // returns size of data set held by the adapter
    @Override
    public int getItemCount() {
       // return imageIDs.length;
        return argsToShow.size();
    }




    // inner class describing views used to  dispaly images
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;  // our images are being displayed in CardView(s)
        private ImageView moreDots;
        private ImageView picture;

        public ViewHolder(CardView itemView) {
            super(itemView);

            cardView=itemView;
            moreDots=(ImageView)cardView.findViewById(R.id.picture_dots);
            picture=(ImageView)cardView.findViewById(R.id.picture);
        }
    }


}
