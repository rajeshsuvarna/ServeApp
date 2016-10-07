package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.OrderDetailsSPActivity;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.adapters.ServiceOrdersAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.OrderListCardSP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrders extends Fragment {

    ServiceOrdersAdapter adapter;
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

    String userid = "", spid = "", type = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_orders, container, false);

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");
        type = spf.getString("typeKey", "");
        spid = spf.getString("spidKey", "");

        total_orders = (TextView) v.findViewById(R.id.t_orders);

        data = fill_with_data(AppConfig.ORDER_LISTING_SP, spid); // pass spid as 2nd parameter here

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewODSP);

        adapter = new ServiceOrdersAdapter(data, mDatasetTypes[0]); //array position is [0] coz card card type is HOME

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), OrderDetailsSPActivity.class);
                i.putExtra("reqid", data.get(position).reqid);
                i.putExtra("spid", spid);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

                Intent i = new Intent(getActivity(), OrderDetailsSPActivity.class);
                i.putExtra("reqid", data.get(position).reqid);
                i.putExtra("spid", spid);
                startActivity(i);
            }
        }));

        return v;
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

                            String a = jsonObject.getString("reqid"), b = jsonObject.getString("spid"),
                                    c = jsonObject.getString("service_name"), d = jsonObject.getString("location"),
                                    e = jsonObject.getString("username"), f = jsonObject.getString("requested_date_time"),
                                    g = jsonObject.getString("accepted");

                            data.add(new OrderListCardSP(a, b, c, d, e, f, g));
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return data;
    }

}