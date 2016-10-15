package com.infouna.serveapp.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.fragments.AboutServeApp;
import com.infouna.serveapp.fragments.AddMyService;
import com.infouna.serveapp.fragments.FAQ;
import com.infouna.serveapp.fragments.HomeFragment;
import com.infouna.serveapp.fragments.MyServiceProfileView;
import com.infouna.serveapp.fragments.NotificationsFragment;
import com.infouna.serveapp.fragments.MyServiceRequest;
import com.infouna.serveapp.fragments.ServiceOrders;
import com.infouna.serveapp.fragments.Support;
import com.infouna.serveapp.fragments.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    int ishomeopen = 1;

    public static SharedPreferences spf;

    public String URL_GET_USER_PROFILE = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=user_profile&userid=";

    private ProgressDialog pDialog;

    TextView name,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        home();
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //drawerLayout.requestDisallowInterceptTouchEvent(true);

        name = (TextView) navigationView.findViewById(R.id.username);
        location = (TextView) findViewById(R.id.location);

        spf = this.getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        String s = spf.getString("useridKey", "Null String");
        Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();

        String userid = s; // replace this with proper userid
        fetch_profile(userid);    // call with userid as parameter



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
                        ishomeopen = 1;
                       home();
                        return true;

                    case R.id.myprofile:
                        ishomeopen = 0;
                        UserProfile userProfilefragment = new UserProfile();
                        android.support.v4.app.FragmentTransaction userProfilefragmentTransaction = getSupportFragmentManager().beginTransaction();
                        userProfilefragmentTransaction.replace(R.id.frame, userProfilefragment);
                        userProfilefragmentTransaction.commit();
                        setTitle("My Profile");
                        return true;
                    case R.id.myservicerequest:
                        ishomeopen = 0;
                        MyServiceRequest myServiceRequestfragment = new  MyServiceRequest();
                        android.support.v4.app.FragmentTransaction myServiceRequestfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        myServiceRequestfragmentTransaction.replace(R.id.frame, myServiceRequestfragment);
                        myServiceRequestfragmentTransaction.commit();
                        setTitle("My Service Request");
                        return true;
                    case R.id.notifications:
                        ishomeopen = 0;
                        NotificationsFragment notificationsFragment = new NotificationsFragment();
                        android.support.v4.app.FragmentTransaction notificationfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        notificationfragmentTransaction.replace(R.id.frame, notificationsFragment);
                        notificationfragmentTransaction.commit();
                        setTitle("Notifications");
                        return true;
                    case R.id.addmyservice:
                        ishomeopen = 0;
                        AddMyService addMyServeFragment = new AddMyService() ;
                        android.support.v4.app.FragmentTransaction addMyServefragmentTransaction = getSupportFragmentManager().beginTransaction();
                        addMyServefragmentTransaction.replace(R.id.frame, addMyServeFragment);
                        addMyServefragmentTransaction.commit();
                        setTitle("Add My Service");
                        return true;

                    case R.id.myserviceprofile:
                        ishomeopen = 0;
                        MyServiceProfileView myServiceProfileViewFragment = new MyServiceProfileView();
                        android.support.v4.app.FragmentTransaction myServiceProfileViewfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        myServiceProfileViewfragmentTransaction.replace(R.id.frame, myServiceProfileViewFragment);
                        myServiceProfileViewfragmentTransaction.commit();
                        setTitle("My Service Profile");
                        return true;

                    case R.id.serviceorders:
                        ishomeopen = 0;
                        ServiceOrders serviceOrdersFragment = new ServiceOrders();
                        android.support.v4.app.FragmentTransaction serviceOrderfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        serviceOrderfragmentTransaction.replace(R.id.frame, serviceOrdersFragment);
                        serviceOrderfragmentTransaction.commit();
                        setTitle("Service Orders");
                        return true;

                    case R.id.aboutserveapp:
                        ishomeopen = 0;
                        AboutServeApp aboutServeAppFragment = new AboutServeApp();
                        android.support.v4.app.FragmentTransaction aboutServeAppfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        aboutServeAppfragmentTransaction.replace(R.id.frame, aboutServeAppFragment);
                        aboutServeAppfragmentTransaction.commit();
                        setTitle("About Serve App");
                        return true;
                    case R.id.faq:
                        ishomeopen = 0;
                        FAQ faqFragment = new FAQ();
                        android.support.v4.app.FragmentTransaction faqfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        faqfragmentTransaction.replace(R.id.frame, faqFragment);
                        faqfragmentTransaction.commit();
                        setTitle("FAQ");
                        return true;
                    case R.id.share:
                        ishomeopen = 0;
                        String message = "Text I want to share.";
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(share, "Share Via"));
                        setTitle("Serve App");
                        return true;
                    case R.id.support:
                        ishomeopen = 0;
                        Support supportFragment = new Support();
                        android.support.v4.app.FragmentTransaction supportfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        supportfragmentTransaction.replace(R.id.frame, supportFragment);
                        supportfragmentTransaction.commit();
                        setTitle("Support");
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

    public void fetch_profile(final String userid) {

       // pDialog.setIndeterminate(true);
        //pDialog.setMessage("Loading...");
        //showDialog();

        String tag_json_obj = "json_obj_req";



        String url = URL_GET_USER_PROFILE;

        url += userid;
        Toast.makeText(HomeActivity.this, url, Toast.LENGTH_LONG).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Toast.makeText(HomeActivity.this, response.toString(), Toast.LENGTH_LONG).show();

                            JSONArray dash = response.getJSONArray("user_profile");

                            JSONObject jsonObject = dash.getJSONObject(0);
                            String a = jsonObject.getString("first_name");

                          //  name.setText(a);


                          //  loadImages(profile_pic);

                           // hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // hideDialog();
                Toast.makeText(HomeActivity.this, "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
               // hideDialog();


            }
        });


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


   /* private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

    public void home()
    {
        HomeFragment homeFragment =  new HomeFragment();
        android.support.v4.app.FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragmentTransaction.replace(R.id.frame, homeFragment);
        homeFragmentTransaction.commit();
        setTitle("Home");
    }

    public void onBackPressed()
    {

        if (ishomeopen != 1)
        {
            openhome();
        }
        else
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
            drawer.closeDrawer(GravityCompat.START);
            closeExit();
        }
    }


    public void closeExit()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application");
        alertDialogBuilder.setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                AppExit();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void AppExit()
    {
        finish();
    }

    public void openhome()
    {
        ishomeopen=1;
        Intent i=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
        setTitle("Serve App");
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