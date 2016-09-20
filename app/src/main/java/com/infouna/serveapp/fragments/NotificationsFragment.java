package com.infouna.serveapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.NotificationCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NotificationsFragment extends Fragment {

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    public String type = "";

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    JsonObjectRequest jsonObjReq;

    public List<NotificationCard> data;

    RVAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        data = fill_with_data();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewHome);

        adapter = new RVAdapter(data, mDatasetTypes[4]); //array position is [4] coz card card type is NOTIFICATION

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();

            }
        }));

        return v;

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index)
    }

    public List<NotificationCard> fill_with_data() {

        data = new ArrayList<>();
        if (type.equals("user")) {

            jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConfig.NOTIFICATION_USER, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray dash = response.getJSONArray("user_notification");

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            data.add(new NotificationCard(jsonObject.getString("user_message"), jsonObject.getString("generated_datetime"),
                                    jsonObject.getString("sp_accepted"), jsonObject.getString("service_name"),
                                    jsonObject.getString("reqid"), jsonObject.getString("userid")));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e)

                    {
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

        } else if (type.equals("sp")) {
            jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConfig.NOTIFICATION_SP, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray dash = response.getJSONArray("sp_notification");

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            data.add(new NotificationCard(jsonObject.getString("sp_message"), jsonObject.getString("generated_datetime"),
                                    jsonObject.getString("request_from"), jsonObject.getString("service_name"),
                                    jsonObject.getString("spid"), jsonObject.getString("reqid"), 0));
                            // last parameter is 0, it is used to distinguish sp notif from user notif
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
        }

        return data;
    }
}