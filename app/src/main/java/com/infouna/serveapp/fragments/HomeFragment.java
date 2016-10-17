package com.infouna.serveapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.ServiceListingActivity;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.HomeCardData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    public String tag_json_arry = "json_array_req";
    JsonObjectRequest jsonObjReq;

    public ScrollView root;
    public Snackbar snackbar;

    public List<HomeCardData> data, d;

    Button all;

    RVAdapter adapter;
    RecyclerView recyclerView;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //root = (ScrollView) v.findViewById(R.id.rootLayout);

        data = fill_with_data();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewHome);

        adapter = new RVAdapter(data, mDatasetTypes[0]); //array position is [0] coz card card type is HOME

        all = (Button) v.findViewById(R.id.search_all);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ServiceListingActivity.class);
                i.putExtra("servicename", "all");
                startActivity(i);
            }
        });

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));

        recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //   Toast.makeText(getActivity(), data.get(position).servicename, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ServiceListingActivity.class);
                i.putExtra("servicename", data.get(position).servicename);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

                //  Toast.makeText(getActivity(), data.get(position).servicename, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ServiceListingActivity.class);
                i.putExtra("servicename", data.get(position).servicename);
                startActivity(i);
            }
        }));

        return v;
    }

    public List<HomeCardData> fill_with_data() {

        data = new ArrayList<>();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_DASHBOARD_SERVICES_HOME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray dash = response.getJSONArray("dashboard_services");

                    if (response.getString("result").equals("1")) {
                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            data.add(new HomeCardData(jsonObject.getInt("service_id"), jsonObject.getString("services"), jsonObject.getString("service_img"), jsonObject.getInt("has_sub_service")));

                        }
                    } else {
                        snackbar = Snackbar.make(getView(), "No data found", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        snackbar.show();
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