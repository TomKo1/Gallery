package com.example.tomek.gallery;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 *
 *  SIMPLE ADAPTER TO BIND IMAGES WITH RECYCLERVIEW
 *
 *
 */

public class RecycleAdapterImage extends RecyclerView.Adapter<RecycleAdapterImage.ViewHolder>{

    private int[]imageIDs;

    //called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item  --- !!!! SQLite !!!!!!
    @Override
    public RecycleAdapterImage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater creates a layout XML file into its corresponding View object
        //static from method obtains the LayoutInflater from the given context
        CardView newCardView=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_card,parent,false);

        return new ViewHolder(newCardView);
    }

    // constructor - to change !!!!!!!!!!!!!!!!!!!!!
    public RecycleAdapterImage(){
        this.imageIDs=new int[10]; // fake 10 images...
    }



    //called by REcyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(RecycleAdapterImage.ViewHolder holder, int position) {
        CardView cardView=holder.cardView;
        ImageView imageView=(ImageView)cardView.findViewById(R.id.picture);
        imageView.setImageDrawable(cardView.getResources().getDrawable(R.drawable.Oreo1));
    }

    // returns size of data set held by the adapter
    @Override
    public int getItemCount() {
        return imageIDs.length;
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
