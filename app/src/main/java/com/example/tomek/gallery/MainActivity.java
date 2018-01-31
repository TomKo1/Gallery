package com.example.tomek.gallery;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
//TODO add option to store pics in DB
//TODO add options to view all pics...

public class MainActivity extends AppCompatActivity {

    // ListView representing options in the drawer
    private ListView listViewOfDrawer;
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

        titles=getResources().getStringArray(R.array.title);

        //operations concerning ListView with options of drawer
        listViewOfDrawer=(ListView)findViewById(R.id.drawer_list_view);
        //setting sample content of Drawer using ArrayAdapter
        listViewOfDrawer.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles));
        //adding listview listener to the ListView representing "drawer" options
        listViewOfDrawer.setOnItemClickListener(new DrawerClickListener());

        //reference to whole DrawerLayout
        wholeDrawer=(DrawerLayout)findViewById(R.id.drawerLayout);


        // config of FragmentManager
        //getFragmentManager  - returns the FragmentManager for interacting with fragments
        //associated with this activity

        /*
         */
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener(){
                    public void onBackStackChanged(){
                      FragmentManager fragmentManager=getFragmentManager();
                      Fragment fragment=fragmentManager.findFragmentByTag("fragment");


                        /*******************************************************88888\
                         *
                         *
                         *
                         *
                         * !!!@@#!!#!@#!@#!!@
                         */




                        if(fragment instanceof PicsFragment ){
                            // actions when PicsFragment was added
                            Toast.makeText(getBaseContext(),"PicsFragment",Toast.LENGTH_SHORT).show();//getBaseContext ???

                        }else if(fragment instanceof PicsChooserFrag){
                            Toast.makeText(getBaseContext(),"PicsChooserFrag",Toast.LENGTH_SHORT).show();//getBaseContext ???

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
              //  Toast.makeText(getApplicationContext(),"Zamknieto Menu",Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView){
              //  Toast.makeText(getApplicationContext(),"Otworzono Menu",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();//declare that options menu has changed , so should be redeclared


                //TODO modify so that keyboard is closed only when it is opened

                ViewUtils.hideKeyBoard(MainActivity.this); //qualified this

            }

        };

        wholeDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();




    }

    // implementation of "OnItemClickListViewListener" interface
    private class DrawerClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> praent,View view, int position,long id){

            selectItem(position);
        }
    }

    private void selectItem(int position){
        Fragment fragmentToPut=null;
        switch(position){
            case 0:
                fragmentToPut=new PicsFragment();
                break;
            case 1:
                fragmentToPut=new PicsChooserFrag();
                Toast.makeText(this,"Choosing...", Toast.LENGTH_LONG).show();
                break;
            default:
                //TODO change this
                fragmentToPut=new PicsFragment();// default
                Toast.makeText(this,"Option doesn't work yet :(",Toast.LENGTH_SHORT).show();
        }
        //we replace the fragment ...
        FragmentTransaction fragTran=getFragmentManager().beginTransaction();
        fragTran.replace(R.id.main_fragment,fragmentToPut,"fragment");
        fragTran.addToBackStack(null);
        //selects standard transaction animation for this transaction
        fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragTran.commit();
        //we close the drawer
        wholeDrawer.closeDrawer(listViewOfDrawer,false);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Options",Toast.LENGTH_SHORT).show();

            //TODO make it like a private method
            /*
            *******************************************************8
             */




        }else if(id == R.id.author){
            Toast.makeText(this,"Author",Toast.LENGTH_SHORT).show();
        }else{
            //showing menu
            Toast.makeText(this,"Menu",Toast.LENGTH_SHORT).show();
            wholeDrawer.openDrawer(Gravity.START);
        }

        return super.onOptionsItemSelected(item);
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
