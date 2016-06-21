package com.infouna.serveapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final int HOME = 0;
    public static final int SCORE = 1;
    public static final int NEWS = 2;

    public String tag_json_arry = "json_array_req";
    JsonObjectRequest jsonObjReq;

    public List<HomeCardData> data;

    private int mDatasetTypes[] = {HOME, SCORE, NEWS, NEWS}; // change array names to different type of card

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

        data = fill_with_data();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewHome);

        adapter = new RVAdapter(data, mDatasetTypes[0]); //array position is [0] coz card card type is HOME

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));

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
    }

    public List<HomeCardData> fill_with_data() {

        data = new ArrayList<>();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_DASHBOARD_SERVICES_HOME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray dash = response.getJSONArray("dashboard_services");

                    JSONObject jsonObject;

                    data.clear();


                    for (int i = 0; i < dash.length(); i++) {

                        jsonObject = dash.getJSONObject(i);

                        data.add(new HomeCardData(jsonObject.getInt("service_id"), jsonObject.getString("services"), jsonObject.getString("service_img"), jsonObject.getInt("has_sub_service")));

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