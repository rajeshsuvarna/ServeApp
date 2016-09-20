package com.infouna.serveapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.HomeCardData;
import com.infouna.serveapp.datamodel.OrderListCardSP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 15-04-2016.
 */
public class OrderListingSPActivity extends Activity {

    RVAdapter adapter;
    RecyclerView recyclerView;

    JsonObjectRequest jsonObjReq;

    public List<OrderListCardSP> data;

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    TextView total_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_sp);

        total_orders = (TextView) findViewById(R.id.t_orders);

        data = fill_with_data(AppConfig.ORDER_LISTING_SP, "452408"); // pass spid as 2nd parameter here

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewODSP);

        adapter = new RVAdapter(data, mDatasetTypes[1]); //array position is [0] coz card card type is HOME

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(OrderListingSPActivity.this, recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(OrderListingSPActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(OrderListingSPActivity.this, "check", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public List<OrderListCardSP> fill_with_data(String url, String spid) {

        data = new ArrayList<>();

        url += "&spid=" + spid;

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray dash = response.getJSONArray("sp_orders");

                    total_orders.setText(response.getString("total_orders"));

                    if (response.getString("result").equals("1")) {

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            data.add(new OrderListCardSP(jsonObject.getString("reqid"), jsonObject.getString("spid"),
                                    jsonObject.getString("service_name"), jsonObject.getString("location"),
                                    jsonObject.getString("username"), jsonObject.getString("requested_date_time"),
                                    jsonObject.getString("accepted")));

                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderListingSPActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return data;
    }
}