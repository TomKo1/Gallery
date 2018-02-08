package com.example.tomek.gallery;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//TODO show Frgment by clicking option in Drawer !!!
//TODO add options of searching pic
//TODO handle screen orientation changes
//TODO add custo ListView in drawer with edittext to search
//TODO !!!!! make optimatization while reading data from DB !!!!
//TODO   !!! make loading work - images get mixed !!!!
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // reference to NavigationView
    private NavigationView navigationView;
    // reference to whole DrawerLayout containing ToolBar, mainFragment and ListView with drawer options
    private DrawerLayout wholeDrawer;
    private String[]titles; // array representing string shown in drawer
    //ActionBarDrawerToggler provides a handy way to tie together the funcionallyty of DrawerLAyout and the framework ActionBar
    //  "makes the hamburger work"
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference to whole DrawerLayout
        wholeDrawer=(DrawerLayout)findViewById(R.id.drawerLayout);

        //reference to NavigationView
        navigationView=(NavigationView)findViewById(R.id.drawer_list_view);

        //listenr for NavigationView
        navigationView.setNavigationItemSelectedListener(this);




        // config of FragmentManager
        //getFragmentManager  - returns the FragmentManager for interacting with fragments
        //associated with this activity






        //Toolbar options
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //displaying hamburger button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // making the hamburger work
        drawerToggle=new ActionBarDrawerToggle(this,wholeDrawer,R.string.app_name,R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView){
              //invalidateOptionsMenu();//declare that options menu has changed , so should be redeclared


                //TODO modify so that keyboard is closed only when it is opened

                ViewUtils.hideKeyBoard(MainActivity.this); //qualified this

            }

        };

        wholeDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

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




    // I created Handler to try to solve drawer lags
    private void addFragment(final Fragment fragmentToAdd, final String string){
        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //we replace the fragment
                FragmentTransaction fragTran=getFragmentManager().beginTransaction();
                fragTran.replace(R.id.main_fragment,fragmentToAdd,"fragment");
                fragTran.addToBackStack(null);
                //selects standard transaction animation for this transaction
                fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragTran.commit();
                ViewUtils.changeToolbarTitle(MainActivity.this,string);
            }
        });

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


    // I created Handler to try to solve drawer lags
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        final int id=item.getItemId();
        Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run(){
                switch(id){
                    case R.id.all_imgs:
                        addFragment(new PicsFragment(),"Gallery");
                        break;
                    case R.id.chooser:
                        addFragment(new PicsChooserFrag(),"Search in device");
                        break;
                    case R.id.searcher:
                        openSearchingDialog();
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"No such operation",Toast.LENGTH_SHORT).show();
                }

            }});
        wholeDrawer.closeDrawer(Gravity.LEFT);
        return true;
    }

    //shows AlertDialog containing Author info
    private void showAuthorInfo(){
            Toast.makeText(this,"Author info",Toast.LENGTH_SHORT).show();
    }

    /*
        called when activity start-ups is complete (after onStart() and onRestoreInstanceStete()


     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    /*
        called by the system when thwe device configuration changes while your activity is running



     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }





}
