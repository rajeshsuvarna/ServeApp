package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.RateServiceActivity;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by infouna
 */
public class UserProfile extends Fragment {

    Context ctx;

    public String URL_GET_USER_PROFILE = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=user_profile&userid=";

    TextView fname, lname, email, mobile, username;
    String profile_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ctx = v.getContext();

        Button bt = (Button) v.findViewById(R.id.button_edit_my_profile);

        username = (TextView) v.findViewById(R.id.username);
        fname = (TextView) v.findViewById(R.id.textview_firstname);
        lname = (TextView) v.findViewById(R.id.textview_lastname);
        email = (TextView) v.findViewById(R.id.textview_email);
        mobile = (TextView) v.findViewById(R.id.textview_mobile);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, RateServiceActivity.class);
                startActivity(i);
            }
        });

        String userid = "566912"; // replace this with proper userid
        fetch_profile(userid);    // call with userid as parameter

        return v;
    }

    public void fetch_profile(final String userid) {
        String tag_json_obj = "json_obj_req";

        URL_GET_USER_PROFILE += userid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_GET_USER_PROFILE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray dash = response.getJSONArray("user_profile");

                            JSONObject jsonObject = dash.getJSONObject(0);

                            username.setText(jsonObject.getString("first_name"));
                            fname.setText(jsonObject.getString("first_name"));
                            lname.setText(jsonObject.getString("last_name"));
                            email.setText(jsonObject.getString("email"));
                            mobile.setText(jsonObject.getString("mobile"));
                            profile_pic = jsonObject.getString("profile_pic");

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
}