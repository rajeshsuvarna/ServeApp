package com.infouna.serveapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddMyService extends Fragment {

    ArrayAdapter<String> adapterONE, adapterTWO;

    JsonObjectRequest jsonObjReq;
    List<String> list1, list2;

    EditText jservdesc, jminprice, jsaddress, jscity, jpin, jweb;
    Button jregisterservice;

    Spinner servicespinner, subservicespinner;

    String service, subservice, service_desc, min_price, address, city, pin, web;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_my_service, container, false);

        servicespinner = (Spinner) v.findViewById(R.id.servicespinner);
        subservicespinner = (Spinner) v.findViewById(R.id.subservicespinner);

        jservdesc = (EditText) v.findViewById(R.id.input_yourservicedescription);
        jminprice = (EditText) v.findViewById(R.id.input_minserviceprice);
        jsaddress = (EditText) v.findViewById(R.id.input_serviceaddress);
        jscity = (EditText) v.findViewById(R.id.input_servicecity);
        jpin = (EditText) v.findViewById(R.id.input_pincode);
        jweb = (EditText) v.findViewById(R.id.input_website);

        jregisterservice = (Button) v.findViewById(R.id.btn_reg);

        fill_spinner(AppConfig.URL_DASHBOARD_SERVICES_HOME, 1, ""); // fill service spinner 1
        setAdapter(1);

        servicespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                adapterTWO = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, Collections.<String>emptyList());
                adapterTWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subservicespinner.setAdapter(adapterTWO);

                fill_spinner(AppConfig.URL_SUB_SEVICES_HOME, 2, servicespinner.getSelectedItem().toString());//servicespinner.getSelectedItem().toString());
                setAdapter(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jregisterservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = servicespinner.getSelectedItem().toString();
                subservice = subservicespinner.getSelectedItem().toString();
                service_desc = jservdesc.getText().toString();
                min_price = jminprice.getText().toString();
                address = jsaddress.getText().toString();
                city = jscity.getText().toString();
                pin = jpin.getText().toString();
                web = jweb.getText().toString();

                register(service, subservice, service_desc, min_price, address, city, pin, web);
            }
        });

        return v;
    }

    private void fill_spinner(String url, final int spin, String servicename) {

        if (spin == 2) {
            url += "&s_name=" + servicename;
        }
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (spin == 1) {
                        JSONArray dash = response.getJSONArray("dashboard_services");

                        JSONObject jsonObject;

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            list1.add(jsonObject.getString("services"));
                        }

                    } else if (spin == 2) {
                        JSONArray dash = response.getJSONArray("sub_service_list");

                        if (response.getString("result").equals("1")) {

                            JSONObject jsonObject;

                            for (int i = 0; i < dash.length(); i++) {

                                jsonObject = dash.getJSONObject(i);

                                list2.add(jsonObject.getString("sub_service_name"));
                            }
                        } else {
                            Toast.makeText(getActivity(), "No subservices", Toast.LENGTH_SHORT).show();
                        }
                    }

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

    private void setAdapter(final int spinner) {
        new Handler().postDelayed(new Runnable() {  //adding a delay to ensure the list has been populated
            @Override
            public void run() {
                if (spinner == 1) {
                    adapterONE = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list1);
                    adapterONE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    servicespinner.setAdapter(adapterONE);

                    fill_spinner(AppConfig.URL_SUB_SEVICES_HOME, 2, (String) servicespinner.getSelectedItem());//servicespinner.getSelectedItem().toString());
                    setAdapter(2);

                } else if (spinner == 2) {

                    adapterTWO = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list2);
                    adapterTWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subservicespinner.setAdapter(adapterTWO);
                }
            }
        }, 2000);

    }

    private void register(String service, String subservice, String service_desc, String min_price, String address, String city, String pin, String web) {


    }


}