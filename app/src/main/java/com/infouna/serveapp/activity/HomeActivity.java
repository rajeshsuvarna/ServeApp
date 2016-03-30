package com.infouna.serveapp.activity;


import android.app.Notification;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.infouna.serveapp.R;
import com.infouna.serveapp.fragments.AboutServeApp;
import com.infouna.serveapp.fragments.AddMyServe;
import com.infouna.serveapp.fragments.FAQ;
import com.infouna.serveapp.fragments.MyServiceRequest;
import com.infouna.serveapp.fragments.Notifications;
import com.infouna.serveapp.fragments.ShareServeApp;
import com.infouna.serveapp.fragments.Support;
import com.infouna.serveapp.fragments.UserProfile;

public class HomeActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our home View;
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "home Selected", Toast.LENGTH_SHORT).show();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.myprofile:
                        UserProfile userProfilefragment = new UserProfile();
                        android.support.v4.app.FragmentTransaction userProfilefragmentTransaction = getSupportFragmentManager().beginTransaction();
                        userProfilefragmentTransaction.replace(R.id.frame, userProfilefragment);
                        userProfilefragmentTransaction.commit();

                        return true;
                    case R.id.myservicerequest:
                        MyServiceRequest myServiceRequestfragment = new  MyServiceRequest();
                        android.support.v4.app.FragmentTransaction myServiceRequestfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        myServiceRequestfragmentTransaction.replace(R.id.frame, myServiceRequestfragment);
                        myServiceRequestfragmentTransaction.commit();
                        return true;
                    case R.id.notifications:
                        Notifications notificationsFragment = new Notifications();
                        android.support.v4.app.FragmentTransaction notificationfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        notificationfragmentTransaction.replace(R.id.frame, notificationsFragment);
                        notificationfragmentTransaction.commit();
                        return true;
                    case R.id.addmyservice:
                        AddMyServe addMyServeFragment = new AddMyServe();
                        android.support.v4.app.FragmentTransaction addMyServefragmentTransaction = getSupportFragmentManager().beginTransaction();
                        addMyServefragmentTransaction.replace(R.id.frame, addMyServeFragment);
                        addMyServefragmentTransaction.commit();
                        return true;
                    case R.id.aboutserveapp:
                        AboutServeApp aboutServeAppFragment = new AboutServeApp();
                        android.support.v4.app.FragmentTransaction aboutServeAppfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        aboutServeAppfragmentTransaction.replace(R.id.frame, aboutServeAppFragment);
                        aboutServeAppfragmentTransaction.commit();
                        return true;
                    case R.id.faq:
                        FAQ faqFragment = new FAQ();
                        android.support.v4.app.FragmentTransaction faqfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        faqfragmentTransaction.replace(R.id.frame, faqFragment);
                        faqfragmentTransaction.commit();
                        return true;
                    case R.id.share:
                        ShareServeApp shareServeAppFragment = new ShareServeApp();
                        android.support.v4.app.FragmentTransaction shareServeAppfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        shareServeAppfragmentTransaction.replace(R.id.frame, shareServeAppFragment);
                        shareServeAppfragmentTransaction.commit();
                        return true;
                    case R.id.support:
                        Support supportFragment = new Support();
                        android.support.v4.app.FragmentTransaction supportfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        supportfragmentTransaction.replace(R.id.frame, supportFragment);
                        supportfragmentTransaction.commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}