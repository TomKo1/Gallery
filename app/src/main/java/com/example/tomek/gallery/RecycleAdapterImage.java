package com.example.tomek.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.tomek.gallery.database.DatabaseDescription;
import com.example.tomek.gallery.fragments.ImageDetail;

import java.io.File;
import java.util.List;

/**
 *
 *  SIMPLE ADAPTER TO BIND IMAGES WITH RECYCLERVIEW
 *
 *
 */

public class RecycleAdapterImage extends RecyclerView.Adapter<RecycleAdapterImage.ViewHolder>{

    // system "short" animation time duration used for "zooming" an image
    private  int mShortAnimationDuration;
    // holds a reference to the current animator
    private Animator mCurrentAnimator;

    private List<MyFunnyImg> argsToShow;

    private Activity activity;






    //called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item  --- !!!! SQLite !!!!!!
    @Override
    public RecycleAdapterImage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater creates a layout XML file into its corresponding View object
        //static from method obtains the LayoutInflater from the given context
        CardView newCardView=(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_card,parent,false);
        ;

        return new ViewHolder(newCardView);
    }





    //TODO we need to pass here all names, desriptions, and Bitmaps from DB
    // constructor - to change !!!!!!!!!!!!!!!!!!!!!
    public RecycleAdapterImage(List<MyFunnyImg>argsToShow, Activity activity){
        this.activity=activity;
        this.argsToShow=argsToShow;
        this.mShortAnimationDuration = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }



    //called by REcyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(RecycleAdapterImage.ViewHolder holder,int position) {
        //getting information about this specific image
        CardView cardView=holder.cardView;
        final ImageView imageView=holder.picture;
        ImageView moreDots=holder.moreDots;

        MyFunnyImg toShow=argsToShow.get(position);
        ImageView expandedImageView = holder.expandedImgView;

        //Bitmap image=argsToShow.get(position).loadBitmap(activity);
        loadBitmap(imageView,toShow);

        String description=toShow.getDescription();
        //String name=toShow.getName();


        //TODO this may be the same as position
        final int position1=position;

        //TODO whis may be separate method
        moreDots.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showPopUpMenu(view, imageView, position1);
            }
        });
        setZoomingAction(imageView,toShow,expandedImageView);



        TextView textView=(TextView)cardView.findViewById(R.id.card_view_des);
        textView.setText(argsToShow.get(position).getName());
        TextView textView2=(TextView)cardView.findViewById(R.id.card_view_text1);
        textView2.setText(argsToShow.get(position).getDescription());
    }

    private void setZoomingAction(final ImageView imgViewSmall,final MyFunnyImg toShow,final ImageView expandedImageView){


        imgViewSmall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(imgViewSmall,toShow.getFileName(),expandedImageView);
            }
        });

    }

    private void zoomImageFromThumb(final ImageView imgViewSmall, String fileName, final ImageView expandedImageView){
        // there is an animation in progress -> cancel it
        if(mCurrentAnimator != null){
            mCurrentAnimator.cancel();
        }

        // load expanded image to ImageView
        String root=null;
        try {
            root = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        }catch(NullPointerException ex){
            ex.printStackTrace();
            Log.e("Error","NullPointerException while loeadin BitMap");
        }




        File dir=new File(root+"/saved_images");
        Log.e("FileName:",fileName);
        File file=new File(dir,fileName);
        Glide.with(activity).load(file).centerCrop().into(expandedImageView);

        // calculate the starting and ending bound for the zoomed-in img
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // starting -> global visible rectangle of the thumbnail (miniaturka)
        // final -> global bound of the container (whole view?) -> recyclerView view
        imgViewSmall.getGlobalVisibleRect(startBounds);
        activity.findViewById(R.id.recyclerView).getGlobalVisibleRect(finalBounds,globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // hide (by setting alpha - channel) the thumbnail and show zoomed-in image
        imgViewSmall.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);


        //Set the pivot point for SCALE_X and SCALE_Y transformations
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);




        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;



        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));



                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imgViewSmall.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        imgViewSmall.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });




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
                showMoreInfo(position);
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

    private void showMoreInfo(int position){

          ImageDetail nextFrag=new ImageDetail();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ViewUtils.BUNDLE_DETAILS_KEY,argsToShow.get(position));
            nextFrag.setArguments(bundle);

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
        //although cetnerCrop may seem uneccessary it wasn't working good
        // without ImageView without this
        Glide.with(activity).load(file).centerCrop().into(imageView);



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
        private ImageView expandedImgView;

        public ViewHolder(CardView itemView) {
            super(itemView);

            cardView=itemView;
            moreDots=(ImageView)cardView.findViewById(R.id.picture_dots);
            picture=(ImageView)cardView.findViewById(R.id.picture);
            expandedImgView=(ImageView)cardView.findViewById(R.id.expanded_image);
        }
    }


}
