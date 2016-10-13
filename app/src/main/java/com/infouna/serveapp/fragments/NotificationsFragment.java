package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.infouna.serveapp.adapters.NotificationAdapter;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.NotificationCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsFragment extends Fragment {

    public static final int HOME = 0;

    private int mDatasetTypes[] = {HOME};

    JsonObjectRequest jsonObjReq;

    public List<NotificationCard> data;

    String userid = "", type = "", spid = "";

    NotificationAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");
        type = spf.getString("typeKey", "");
        if (type.equals("SP")) {
            spid = spf.getString("spidKey", "");
        }

        data = fill_with_data(AppConfig.NOTIFICATION_USER, AppConfig.NOTIFICATION_SP);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewNotif);

        adapter = new NotificationAdapter(data, mDatasetTypes[0]); //array position is [4] coz card card type is NOTIFICATION

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return v;
    }

    public List<NotificationCard> fill_with_data(String user_url, String sp_url) {

        data = new ArrayList<>();
        if (type.equals("USER")) {
            user_url += userid;

            jsonObjReq = new JsonObjectRequest(Request.Method.GET, user_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray dash = response.getJSONArray("user_notification");

                        if (response.getString("result").equals("1")) {

                            JSONObject jsonObject;

                            data.clear();

                            for (int i = 0; i < dash.length(); i++) {

                                jsonObject = dash.getJSONObject(i);

                                String a = jsonObject.getString("user_message"), b = jsonObject.getString("generated_datetime"),
                                        c = jsonObject.getString("sp_accepted"), d = jsonObject.getString("service_name"),
                                        e = jsonObject.getString("reqid"), f = jsonObject.getString("userid");

                                data.add(new NotificationCard(a, b, c, d, e, f));
                            }
                        } else {

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

        } else if (type.equals("SP")) {
            sp_url += spid;
            jsonObjReq = new JsonObjectRequest(Request.Method.GET, sp_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray dash = response.getJSONArray("sp_notification");

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            String a = jsonObject.getString("sp_message"), b = jsonObject.getString("generated_datetime"),
                                    c = jsonObject.getString("request_from"), d = jsonObject.getString("service_name"),
                                    e = jsonObject.getString("spid"), f = jsonObject.getString("reqid");

                            data.add(new NotificationCard(a, b,
                                    c, d,
                                    e, f, 0));
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