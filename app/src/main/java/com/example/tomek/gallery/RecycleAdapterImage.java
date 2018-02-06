package com.example.tomek.gallery;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(RecycleAdapterImage.ViewHolder holder, int position) {
        //getting information about this specific image
        CardView cardView=holder.cardView;
        ImageView imageView=(ImageView)cardView.findViewById(R.id.picture);

        MyFunnyImg toShow=argsToShow.get(position);

        //TODO here I want to use Glide to load images
        //Bitmap image=argsToShow.get(position).loadBitmap(activity);
        loadBitmap(imageView,toShow);

        String description=toShow.getDescription();
        String name=toShow.getName();



        //imageView.setImageBitmap(image);
        TextView textView=(TextView)cardView.findViewById(R.id.card_view_des);
        textView.setText(description);//description
    }

    // loads BitMap into ImageView using glide
    private void loadBitmap(ImageView imageView,MyFunnyImg toShow){

        String root=null;
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while loeadin BitMap");
        }




        File dir=new File(root+"/saved_images");

        File file=new File(dir,toShow.getFileName());
        //Log.e("Loading: ", toShow.getPath());
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

        public ViewHolder(CardView itemView) {
            super(itemView);

            cardView=itemView;
        }
    }


}
