package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.plus.model.people.Person;
import com.infouna.serveapp.R;
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

public class ServiceListingActivity extends Activity {

    RVAdapter adapter;
    RecyclerView recyclerView;

    JsonObjectRequest jsonObjReq, jsonObjReqfav;

    public List<ServiceListCard> data;

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    TextView total_orders;

    public String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_listing);
        Intent i = getIntent();
        Bundle b = i.getExtras();

        total_orders = (TextView) findViewById(R.id.total_service_listing);

        data = fill_with_data(AppConfig.SERVICE_LISTING_URL, b.getString("servicename")); // pass servicename/keyword as 2nd parameter here

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewServiceListing);

        adapter = new RVAdapter(data, mDatasetTypes[3]); //array position is [3] coz card card type is SERVICELIST

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(ServiceListingActivity.this, recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(ServiceListingActivity.this, ServiceDetailsActivity.class);
                i.putExtra("servicename", data.get(position).service_name);
                i.putExtra("fav", data.get(position).favourite);
                i.putExtra("uid", data.get(position).userid);
                i.putExtra("picture", data.get(position).banner_picture);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent i = new Intent(ServiceListingActivity.this, ServiceDetailsActivity.class);
                i.putExtra("servicename", data.get(position).service_name);
                i.putExtra("fav", data.get(position).favourite);
                i.putExtra("uid", data.get(position).userid);
                i.putExtra("picture", data.get(position).banner_picture);
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

                    if (res.equals("1")) {

                        JSONArray dash = response.getJSONArray("search_listing");
                        String fav = "";

                        total_orders.setText(response.getString("total_services"));

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            fav = check_favourite(jsonObject.getString("userid"), jsonObject.getString("service_name"), AppConfig.CHECK_FAVOURITE);

                            data.add(new ServiceListCard(jsonObject.getString("userid"), jsonObject.getString("service_providerid"),
                                    jsonObject.getString("service_name"), "http://serveapp.in/imgupload/uploadedimages/" + jsonObject.getString("banner_picture"),
                                    jsonObject.getString("service_tag"), jsonObject.getString("service_location"),
                                    jsonObject.getString("service_price"), jsonObject.getString("confirmed"),
                                    jsonObject.getString("total_ratings"), jsonObject.getString("total_reviews"), fav));
                        }
                    } else if (res.equals("0")) {
                        total_orders.setText("0");
                       // Toast.makeText(ServiceListingActivity.this, "else" + response.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
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
}