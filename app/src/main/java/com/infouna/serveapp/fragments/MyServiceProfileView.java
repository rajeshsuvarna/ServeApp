package com.infouna.serveapp.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.ServiceProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyServiceProfileView extends Fragment {
    TextView jyourservice, jsubservice, jservicedesc, jminprice, jaddress, jweb;
    Button jedit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_profile_view, container, false);

        jyourservice = (TextView) v.findViewById(R.id.textview_your_service);
        jsubservice = (TextView) v.findViewById(R.id.textview_sub_service);
        jservicedesc = (TextView) v.findViewById(R.id.textview_service_desc);
        jminprice = (TextView) v.findViewById(R.id.textview_min_service_price);
        jaddress = (TextView) v.findViewById(R.id.textview_address);
        jweb = (TextView) v.findViewById(R.id.textview_your_website);

        jedit = (Button) v.findViewById(R.id.button_edit_my_service_profile);

        jedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fetch_profile("188254");

        return v;
    }

    public void fetch_profile(String userid) {
        String tag_json_obj = "json_obj_req";

        String url = AppConfig.URL_GET_SERVICE_PROFILE;

        url += userid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String res = response.getString("result");

                            if (res.equals("0")) {
                                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray dash = response.getJSONArray("sp_profile");

                                JSONObject jsonObject = dash.getJSONObject(0);

                                ServiceProfile sp = new ServiceProfile(
                                        jsonObject.getString("first_name"), jsonObject.getString("last_name"),
                                        jsonObject.getString("email"), jsonObject.getString("profile_pic"),
                                        jsonObject.getString("address"), jsonObject.getString("logo_path"),
                                        jsonObject.getString("shop_photos"), jsonObject.getString("website"),
                                        jsonObject.getString("location"), jsonObject.getString("service_id"),
                                        jsonObject.getString("service_name"), jsonObject.getString("sub_service_name"),
                                        jsonObject.getString("tags"), jsonObject.getString("service_price"));

                                jyourservice.setText(sp.service_name);
                                jsubservice.setText(sp.sub_service_name);
                                jservicedesc.setText("");     // nothing in URL
                                jminprice.setText(sp.service_price);
                                jaddress.setText(sp.address);
                                jweb.setText(sp.website);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                , new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );


// Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}