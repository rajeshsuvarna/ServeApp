package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.infouna.serveapp.activity.OrderDetailsUserActivity;
import com.infouna.serveapp.adapters.MyServiceRequestsAdapter;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.OrderListCardUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyServiceRequest extends Fragment {
    MyServiceRequestsAdapter adapter;
    RecyclerView recyclerView;

    JsonObjectRequest jsonObjReq;

    public List<OrderListCardUser> data;

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    TextView total_orders;

    String reqid = "", userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_service_request, container, false);

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");

        total_orders = (TextView) v.findViewById(R.id.torders_ODU);

        data = fill_with_data(AppConfig.ORDER_LISTING_USER, userid); // pass userid as 2nd parameter from SHARED PREFERENCE

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewODU);

        adapter = new MyServiceRequestsAdapter(data, mDatasetTypes[0]); //array position is [0] coz card card type is HOME

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Toast.makeText(getActivity(), "uid" + userid, Toast.LENGTH_SHORT).show();

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), OrderDetailsUserActivity.class);
                i.putExtra("reqid", data.get(position).reqid);
                i.putExtra("userid", userid);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
                Intent i = new Intent(getActivity(), OrderDetailsUserActivity.class);
                i.putExtra("reqid", data.get(position).reqid);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        }));

        return v;
    }

    public List<OrderListCardUser> fill_with_data(String url, String uid) {

        data = new ArrayList<>();

        url += uid;

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("result");

                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    JSONArray dash = response.getJSONArray("orders");

                    if (res.equals("1")) {

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            reqid = jsonObject.getString("request_id");

                            total_orders.setText(jsonObject.getString("total_orders"));

                            String b = jsonObject.getString("service_name"), c = jsonObject.getString("location"),
                                    d = jsonObject.getString("requested_date_time"), e = jsonObject.getString("accepted");

                            data.add(new OrderListCardUser(reqid, b, c, d, e));

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