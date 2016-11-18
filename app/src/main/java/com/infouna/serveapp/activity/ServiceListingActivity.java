package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.adapters.ServiceListingAdapter;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.ServiceListCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 22-08-2016.
 */

public class ServiceListingActivity extends AppCompatActivity {

    ServiceListingAdapter adapter;
    RecyclerView recyclerView;

    JsonObjectRequest jsonObjReq, jsonObjReqfav;

    public static final String ser_prov_user_id = "service_uid_Key";
    public static final String ser_prov_id = "service_id_Key";
    public static final String service_name = "service_name_key";
    public static final String sub_service_name = "sub_service_name_key";
    public static final String service_title = "service_title_key";
    public static final String ser_prov_ban_pic = "ser_prov_ban_pic_Key";
    public static final String ser_prov_confirmed = "ser_prov_confirm_Key";
    public static final String ser_prov_total_rating = "ser_prov_total_rating_Key";
    public static final String ser_prov_total_review = "ser_prov_total_review_Key";
    public static final String ser_prov_user_fav = "ser_prov_user_fav_Key";

    public List<ServiceListCard> data;

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private Toolbar toolbar;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    TextView total_orders;

    public String s_name;

    public String result = "";

    public static SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_listing);

        spf = this.getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Service Listing");


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //Intent i = getIntent();
        //  Bundle b = i.getExtras();

        final String serv_name = spf.getString("servicenameKey", "Null String");

        //  Toast.makeText(ServiceListingActivity.this, serv_name, Toast.LENGTH_SHORT).show();

        total_orders = (TextView) findViewById(R.id.total_service_listing);

        //Toast.makeText(ServiceListingActivity.this, b.getString("servicename"), Toast.LENGTH_SHORT).show();

        data = fill_with_data(AppConfig.SERVICE_LISTING_URL, serv_name); // pass servicename/keyword as 2nd parameter here

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewServiceListing);

        adapter = new ServiceListingAdapter(data, mDatasetTypes[0]); //array position is [3] coz card card type is SERVICELIST

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        //   recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(ServiceListingActivity.this));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(ServiceListingActivity.this, recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(ServiceListingActivity.this, ServiceDetailsActivity.class);

                SharedPreferences.Editor editor = spf.edit();
                editor.putString(ser_prov_user_id, data.get(position).userid);
                editor.putString(ser_prov_id, data.get(position).service_providerid);
                editor.putString(service_name, data.get(position).service_name);
                editor.putString(sub_service_name, data.get(position).sub_service_name);
                editor.putString(service_title, data.get(position).service_title);
                editor.putString(ser_prov_ban_pic, data.get(position).banner_picture);
                editor.putString(ser_prov_confirmed, data.get(position).confirmed);
                editor.putString(ser_prov_total_rating, data.get(position).total_ratings);
                editor.putString(ser_prov_total_review, data.get(position).total_reviews);
                editor.putString(ser_prov_user_fav, data.get(position).favourite);

                editor.commit();

                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent i = new Intent(ServiceListingActivity.this, ServiceDetailsActivity.class);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString(ser_prov_user_id, data.get(position).userid);
                editor.putString(ser_prov_id, data.get(position).service_providerid);
                editor.putString(service_name, data.get(position).service_name);
                editor.putString(sub_service_name, data.get(position).sub_service_name);
                editor.putString(service_title, data.get(position).service_title);
                editor.putString(ser_prov_ban_pic, data.get(position).banner_picture);
                editor.putString(ser_prov_confirmed, data.get(position).confirmed);
                editor.putString(ser_prov_total_rating, data.get(position).total_ratings);
                editor.putString(ser_prov_total_review, data.get(position).total_reviews);

                editor.commit();

                startActivity(i);
            }
        }));
    }

    public List<ServiceListCard> fill_with_data(String url, String keyword) {

        data = new ArrayList<>();

        url += keyword;

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String res = response.getString("result");

                    JSONArray dash = response.getJSONArray("search_listing");

                    if (res.equals("1")) {

                        String fav = "0";

                        String t = response.getString("total_services");

                        total_orders.setText(t);

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            String userid = jsonObject.getString("userid");
                            String service_providerid = jsonObject.getString("service_providerid");
                            String service_name = jsonObject.getString("service_name");
                            String sub_service_name = jsonObject.getString("sub_service_name");
                            String service_title = jsonObject.getString("service_title");
                            String banner_picture = jsonObject.getString("banner_picture");
                            String confirmed = jsonObject.getString("confirmed");
                            String total_ratings = jsonObject.getString("total_ratings");
                            String total_reviews = jsonObject.getString("total_reviews");

                         //   Toast.makeText(ServiceListingActivity.this, service_providerid, Toast.LENGTH_SHORT).show();
                            fav = check_favourite(userid, s_name, AppConfig.CHECK_FAVOURITE);

                            data.add(new ServiceListCard(userid, service_providerid, service_name, service_title, sub_service_name, banner_picture, confirmed, total_ratings, total_reviews, fav));

                        }
                        adapter.notifyDataSetChanged();
                    } else if (res.equals("0")) {
                        total_orders.setText("0");
                        Toast.makeText(ServiceListingActivity.this, "No services found..Come back later", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return data;
    }

    public String check_favourite(String uid, String sname, String URL) {

        URL += "&spid=" + uid + "&s_name=" + sname;

        jsonObjReqfav = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getString("result").equals("1")) {

                        JSONArray dash = response.getJSONArray("user_favourite_service");

                        JSONObject jsonObject = dash.getJSONObject(0);

                        result = jsonObject.getString("liked");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceListingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReqfav);

        return result;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}