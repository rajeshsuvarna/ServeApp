package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Darshan on 13-04-2016.
 */

public class ServiceDetailsActivity extends AppCompatActivity {

    TextView servicename, review_count, address;
    Button request;
    ImageView btnfav, stars[] = new ImageView[5];
    LinearLayout background;

    private Toolbar toolbar;

    public static SharedPreferences spf;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    String fav, ratin;
    int rate;

    String userid, spid, s_name, s_sub_name, max_budget, location, req_dt, add, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Service Details");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "Null String");


        Intent i = getIntent();
        Bundle b = i.getExtras();


        servicename = (TextView) findViewById(R.id.sd_sname);
        review_count = (TextView) findViewById(R.id.sd_reviewcount);
        address = (TextView) findViewById(R.id.sd_address);
        background = (LinearLayout) findViewById(R.id.sd_background);

        request = (Button) findViewById(R.id.btn_sd_request);

        btnfav = (ImageView) findViewById(R.id.sd_favourite);

        stars[0] = (ImageView) findViewById(R.id.star_1);
        stars[1] = (ImageView) findViewById(R.id.star_2);
        stars[2] = (ImageView) findViewById(R.id.star_3);
        stars[3] = (ImageView) findViewById(R.id.star_4);
        stars[4] = (ImageView) findViewById(R.id.star_5);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ServiceDetailsActivity.this, RequestNewServiceActivity.class);
                i.putExtra("Hint_max", "max");
                i.putExtra("Hint_loc", "loc");
                i.putExtra("Hint_date", "date");
                i.putExtra("Hint_add", "add");
                i.putExtra("Hint_desc", "desc");
                startActivityForResult(i, 1);

            }
        });

        s_name = b.getString("servicename");

        load_order_details(b.getString("uid"), b.getString("servicename"), b.getString("fav"), AppConfig.SERVICE_DETAILS_URL);
        loadImages("http://serveapp.in/imgupload/uploadedimages/123456Hurt.jpg");
        //  loadImages(b.getString("picture"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // this is overriden inorder to recieve data
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Bundle b = data.getExtras();

            max_budget = b.getString("max");
            location = b.getString("loc");
            req_dt = b.getString("date");
            add = b.getString("add");
            desc = b.getString("desc");

            request_service(userid, spid, s_name, s_sub_name, max_budget, location, req_dt, add, desc, AppConfig.SERVICE_REQUEST);

        } catch (Exception ex) {
            Toast.makeText(ServiceDetailsActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void request_service(String userid, String s, String s_name, String s_sub_name, String max_budget, String location, String req_dt, String add, String desc, String URL) {
        URL += "&userid=" + userid + "&spid=" + s + "&s_name=" + s_name + "&s_sub_name=" + s_sub_name + "&max_budget=" + max_budget + "&location="
                + location + "&req_dt=" + req_dt + "&add=" + add + "&desc=" + desc;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String reqid = response.getString("reqid");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void load_order_details(String uid, final String sname, final String fav, String url) {

        url += "&userid=" + uid + "&s_name=" + sname;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray dash = response.getJSONArray("service_details");

                            JSONObject jsonObject = dash.getJSONObject(0);

                            String reviews = jsonObject.getString("total_reviews");

                            servicename.setText(sname);

                            if (Integer.parseInt(reviews) > 0) {
                                review_count.setText(reviews + "reviews");
                            } else {
                                review_count.setText("0 reviews");
                            }

                            spid = jsonObject.getString("service_providerid");
                            Toast.makeText(ServiceDetailsActivity.this, spid, Toast.LENGTH_SHORT).show();

                            s_sub_name = jsonObject.getString("sub_service_name");

                            address.setText(jsonObject.getString("location"));

                            rater(3);
                            //rater(Integer.parseInt(jsonObject.getString("total_ratings")));

                            if (fav.equals("1")) {
                                btnfav.setImageResource(R.mipmap.ic_like_selected);
                            } else {
                                btnfav.setImageResource(R.mipmap.ic_like_deselected);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void rater(int rating) {
        clear_rating();
        for (int i = 0; i < rating; i++) {
            stars[i].setImageResource(R.mipmap.ic_star_selected);
        }
    }

    public void clear_rating() {
        for (int i = 0; i < 5; i++)
            stars[i].setImageResource(R.mipmap.ic_star_deselected);
    }

    private void loadImages(String urlThumbnail) {

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    background.setBackground(new BitmapDrawable(response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,ServiceListingActivity.class);
        startActivity(back);
        finish();
    }
}