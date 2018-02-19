package com.example.tomek.gallery;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.*;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout wholeDrawer;
    private String[]titles; // array representing string shown in drawer
    private ActionBarDrawerToggle drawerToggle;

    private Fragment fragmentToAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wholeDrawer=(DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView=(NavigationView)findViewById(R.id.drawer_list_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle=new ActionBarDrawerToggle(this,wholeDrawer,R.string.app_name,R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);

                //   addFragment(fragmentToAdd,"123");
            }



            @Override
            public void onDrawerOpened(View drawerView){
                //TODO modify so that keyboard is closed only when it is opened

                ViewUtils.hideKeyBoard(MainActivity.this); //qualified this

            }

        };

        wholeDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        fragmentToAdd=new PicsFragment();
        addFragment(fragmentToAdd,"Gallery"); //TODO this may cause some problems
    }




    private void openSearchingDialog(){
       final Dialog dialog=new Dialog(this);     // 2 arg may be a style
        dialog.setContentView(R.layout.searching_dialog);
        dialog.setTitle("Search in Google");

        Button okBtn=(Button)dialog.findViewById(R.id.searching_dial_OK);
        Button canBtn=(Button)dialog.findViewById(R.id.searching_dial_Cancle);

        okBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText edText=(EditText)dialog.findViewById(R.id.searching_phrase);
                String searchPhras=edText.getText().toString();
                Uri uri=Uri.parse("http://www.google.com/#q="+searchPhras);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        canBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    private void addFragment(final Fragment fragmentToAdd, final String string){
                FragmentTransaction fragTran=getFragmentManager().beginTransaction();
                fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragTran.replace(R.id.main_fragment,fragmentToAdd,"fragment");
                fragTran.addToBackStack(null);
                fragTran.commit();
                ViewUtils.changeToolbarTitle(MainActivity.this,string);

   }


    // this method places menu options to menu
    //called only once, the first time the options menu is created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if(id == R.id.author){
             showAuthorInfo();
        }else{
             wholeDrawer.openDrawer(Gravity.LEFT);
         }

        return super.onOptionsItemSelected(item);
    }


    //shows info about author
    private void showAuthorInfo(){
            String link="<a href=\"http://www.github.com/TomKo1\">Other projects</a>";
            String message="Created by: TomKo1 - "+link;
            //TODO - depreciated !
            Spanned myMessage= Html.fromHtml(message);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(myMessage);
            builder.setCancelable(true);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                       // we just only have button which dismisses
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.show();
                TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
                msgTxt.setMovementMethod(LinkMovementMethod.getInstance());
        }





    // I created Handler to try to solve drawer lags
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
            int id=item.getItemId();
            Handler handler=new Handler();
            String nameOfFrag="Gallery";
                switch(id){
                    case R.id.all_imgs:
                            fragmentToAdd=new PicsFragment();
                            nameOfFrag="Main Gallery";
                            break;
                    case R.id.chooser:
                            fragmentToAdd=new PicsChooserFrag();
                            nameOfFrag="Searching device";
                            break;
                    case R.id.searcher:
                        nameOfFrag="Searching Internet";
                        openSearchingDialog();
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"No such operation",Toast.LENGTH_SHORT).show();
                }
               if(!nameOfFrag.equals("Gallery")) addFragment(fragmentToAdd,nameOfFrag);

        // this delay may depend on the device the app runs but I tested it with
        // few devices and it works better then placing it in onDrawerClose method and facing
        // more visible (for user) loading problems
        // some reference: https://vikrammnit.wordpress.com/2016/03/28/facing-navigation-drawer-item-onclick-lag/
        // the delay is described in the end -> useful links to Stack also there
        //TODO maybe change this to onDrawerClosed method with "loading screen"
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wholeDrawer.closeDrawer(Gravity.LEFT);

            }
        },25);

        return true;
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }





}
