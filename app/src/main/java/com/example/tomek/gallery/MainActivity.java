package com.example.tomek.gallery;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

        //TODO drawer is lagging
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener(){
                    public void onBackStackChanged(){
                      FragmentManager fragmentManager=getFragmentManager();
                      Fragment fragment=fragmentManager.findFragmentByTag("fragment");



                        if(fragment instanceof PicsFragment ){
                            // actions when PicsFragment was added to the BackStack

                        }else if(fragment instanceof PicsChooserFrag){
                           // actions when PicsChooserFrag was added to the BackStack

                        }

                    }
                }
        );




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

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id=item.getItemId();
        switch(id){
            case R.id.all_imgs:
                addFragment(new PicsFragment());
                break;
            case R.id.chooser:
                addFragment(new PicsChooserFrag());
                break;
            case R.id.searcher:
                openSearchingDialog();
                break;
            default:
                return false;
        }
        wholeDrawer.closeDrawer(Gravity.LEFT);
        return true;
    }



    private void openSearchingDialog(){

        AlertDialog.Builder adBuilder= new AlertDialog.Builder(this);
        View dialogView=getLayoutInflater().inflate(R.layout.searching_dialog,null);

        adBuilder.setTitle("Search for images in Google");
        adBuilder.setView(dialogView);
        adBuilder.setPositiveButton("Ok",new  DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //TODO make here searching intent
                Toast.makeText(MainActivity.this,"Button clicked",Toast.LENGTH_SHORT).show();
            }
        });
        adBuilder.setNegativeButton("Cancel",null);

        AlertDialog dialog=adBuilder.create();
        dialog.show();
    }

    private void addFragment(Fragment fragmentToAdd){
        //we replace the fragment
        FragmentTransaction fragTran=getFragmentManager().beginTransaction();
        fragTran.replace(R.id.main_fragment,fragmentToAdd,"fragment");
        fragTran.addToBackStack(null);
        //selects standard transaction animation for this transaction
        fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragTran.commit();
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
             Toast.makeText(this,"How about using menu?",Toast.LENGTH_SHORT).show();
             wholeDrawer.openDrawer(Gravity.START);
        }

        return super.onOptionsItemSelected(item);
    }


    //shows AlertDialog containing Author info
    private void showAuthorInfo(){

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
