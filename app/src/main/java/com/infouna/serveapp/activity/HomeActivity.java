package com.infouna.serveapp.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.fragments.AboutServeApp;
import com.infouna.serveapp.fragments.AddMyService;
import com.infouna.serveapp.fragments.FAQ;
import com.infouna.serveapp.fragments.HomeFragment;
import com.infouna.serveapp.fragments.MyServiceProfileView;
import com.infouna.serveapp.fragments.NotificationBaseFragment;
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

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public static SharedPreferences spf;

    public static final String fname = "fnameKey";
    public static final String lname = "lnameKey";
    public static final String email = "emailKey";
    public static final String phone = "phoneKey";
    public static final String profile = "profilepicKey";

    public String URL_GET_USER_PROFILE = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=user_profile&userid=";

    TextView name, location;
    de.hdodenhof.circleimageview.CircleImageView Picholder;
    String pp;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

        verifyStoragePermissions(HomeActivity.this);

        View header = navigationView.getHeaderView(0);

        name = (TextView) header.findViewById(R.id.username);
        //location = (TextView) findViewById(R.id.location);

        spf = this.getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        String u = spf.getString("useridKey", "Null String");

        String userid = u; // replace this with proper userid
        fetch_profile(userid);    // call with userid as parameter

        Picholder = (de.hdodenhof.circleimageview.CircleImageView) header.findViewById(R.id.drawer_image);

        pp = spf.getString(profile, "Null String");
        String[] split = pp.split("/");

        if (split.length == 5) {

            Picholder.setImageResource(R.drawable.default_user_photo);
        } else if (pp.contains("serveapp")) {

            loadImages(pp);
        } else {
            Picholder.setImageResource(R.drawable.default_user_photo);
        }


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
                        MyServiceRequest myServiceRequestfragment = new MyServiceRequest();
                        android.support.v4.app.FragmentTransaction myServiceRequestfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        myServiceRequestfragmentTransaction.replace(R.id.frame, myServiceRequestfragment);
                        myServiceRequestfragmentTransaction.commit();
                        setTitle("My Service Request");
                        return true;
                    case R.id.notifications:
                        ishomeopen = 0;
                        NotificationBaseFragment notificationsFragment = new NotificationBaseFragment();
                        android.support.v4.app.FragmentTransaction notificationfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        notificationfragmentTransaction.replace(R.id.frame, notificationsFragment);
                        notificationfragmentTransaction.commit();
                        setTitle("Notifications");
                        return true;
                    case R.id.addmyservice:
                        ishomeopen = 0;
                        Intent i = new Intent(HomeActivity.this, AddMyServiceActivity.class);
                        startActivity(i);
                        /*
                        AddMyService addMyServeFragment = new AddMyService();
                        android.support.v4.app.FragmentTransaction addMyServefragmentTransaction = getSupportFragmentManager().beginTransaction();
                        addMyServefragmentTransaction.replace(R.id.frame, addMyServeFragment);
                        addMyServefragmentTransaction.commit();
                        setTitle("Add My Service");
                        */
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
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                pp = spf.getString(profile, "Null String");
                String[] split = pp.split("/");


                if (split.length == 5) {

                    Picholder.setImageResource(R.drawable.default_user_photo);
                } else if (pp.contains("serveapp")) {

                    loadImages(pp);
                } else {
                    Picholder.setImageResource(R.drawable.default_user_photo);
                }

                name.setText(spf.getString(fname, "Null String") + " " + spf.getString(lname, "Null String"));
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dash = response.getJSONArray("user_profile");
                            JSONObject jsonObject = dash.getJSONObject(0);
                            String fn = jsonObject.getString("first_name");
                            String ln = jsonObject.getString("last_name");
                            String mail = jsonObject.getString("email");
                            String mob = jsonObject.getString("mobile");
                            String pic = jsonObject.getString("profile_pic");
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString(fname, fn);
                            editor.putString(lname, ln);
                            editor.putString(email, mail);
                            editor.putString(phone, mob);
                            editor.putString(profile, pic);
                            editor.commit();
                            name.setText(fn + " " + ln);

                            pp = spf.getString(profile, "Null String");
                            String[] split = pp.split("/");

                            if (split.length == 5) {

                                Picholder.setImageResource(R.drawable.default_user_photo);
                            } else if (pp.contains("serveapp")) {

                                loadImages(pp);
                            } else {
                                Picholder.setImageResource(R.drawable.default_user_photo);
                            }

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

    public void home() {
        HomeFragment homeFragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragmentTransaction.replace(R.id.frame, homeFragment);
        homeFragmentTransaction.commit();
        setTitle("Home");
    }

    @Override
    public void onBackPressed() {
        if (ishomeopen != 1) {
            openhome();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
            drawer.closeDrawer(GravityCompat.START);
            closeExit();
        }
    }


    public void closeExit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application");
        alertDialogBuilder.setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppExit();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void AppExit() {
        finish();
    }

    public void openhome() {
        ishomeopen = 1;
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
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
        if (id == R.id.action_logout) {

            Toast.makeText(this, "Bye..!! See you soon", Toast.LENGTH_SHORT).show();

            spf = this.getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.action_share) {

            String message = "Text I want to share.";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Share Via"));
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadImages(String urlThumbnail) {
        // urlThumbnail = "http://"+urlThumbnail;

        //Toast.makeText(HomeActivity.this, urlThumbnail, Toast.LENGTH_SHORT).show();

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Picholder.setImageDrawable(null); // to clear the static background
                    //Picholder.setBackground(new BitmapDrawable(response.getBitmap()));
                    Picholder.setImageDrawable(new BitmapDrawable(response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}