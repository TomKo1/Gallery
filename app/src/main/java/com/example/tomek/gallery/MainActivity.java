package com.example.tomek.gallery;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // reference to DrawerLayout to show app options
    private ListView searchingDrawer;
    private DrawerLayout wholeDrawer;
    private String[]titles; // array representing string shown in drawer
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles=getResources().getStringArray(R.array.title);
        searchingDrawer=(ListView)findViewById(R.id.searchingDrawer);
        //setting sample content of Drawer using ArrayAdapter
        searchingDrawer.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles));
        //reference to whole DrawerLayout
        wholeDrawer=(DrawerLayout)findViewById(R.id.drawerLayout);





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


            }

            @Override
            public void onDrawerOpened(View drawerView){
              //  Toast.makeText(getApplicationContext(),"Otworzono Menu",Toast.LENGTH_SHORT).show();
            }

        };

        wholeDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

       // wholeDrawer.openDrawer(Gravity.START);


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
            Toast.makeText(this,"Opcje",Toast.LENGTH_SHORT).show();
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
