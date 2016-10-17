package com.infouna.serveapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
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

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.infouna.serveapp.activity.LoginActivity.MyPREFERENCES;

public class AddMyService extends Fragment {

    ArrayAdapter<String> adapterONE, adapterTWO;

    JsonObjectRequest jsonObjReq;
    List<String> list1, list2;

    public static final String MyPREFERENCES = "MyPrefs.txt";

/*
    @Bind(R.id.input_add_yourservicedescription) EditText jservdesc;
    @Bind(R.id.input_add_minserviceprice) EditText jminprice;
    @Bind(R.id.input_add_serviceaddress) EditText jsaddress;
    @Bind(R.id.input_add_servicecity) EditText jscity;
    @Bind(R.id.input_add_pincode) EditText jpin;
    @Bind(R.id.input_add_website) EditText jweb;

*/

    EditText jservice, jsubservice, jservdesc, jminprice, jsaddress, jscity, jpin, jweb;
    Button jregisterservice;
    private ProgressDialog pDialog;

    public static SharedPreferences spf;

    // Spinner servicespinner, subservicespinner;

    String userid, ban_pic, shop_pic, loc, service, subservice, service_desc, min_price, address, city, pin, web;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_my_service, container, false);

        ButterKnife.bind(this.getActivity(), v);

        spf = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "Null String");

        Toast.makeText(getActivity(), userid + "test", Toast.LENGTH_SHORT).show();

        //servicespinner = (Spinner) v.findViewById(R.id.servicespinner);
        //subservicespinner = (Spinner) v.findViewById(R.id.subservicespinner);

        jservice = (EditText) v.findViewById(R.id.input_add_service);
        jsubservice = (EditText) v.findViewById(R.id.input_add_sub_service);
        jservdesc = (EditText) v.findViewById(R.id.input_add_yourservicedescription);
        jsaddress = (EditText) v.findViewById(R.id.input_add_serviceaddress);
        jminprice = (EditText) v.findViewById(R.id.input_add_minserviceprice);
        jpin = (EditText) v.findViewById(R.id.input_add_pincode);
        jscity = (EditText) v.findViewById(R.id.input_add_servicecity);
        jweb = (EditText) v.findViewById(R.id.input_add_website);
        jregisterservice = (Button) v.findViewById(R.id.btn_reg_add);

      /*  fill_spinner(AppConfig.URL_DASHBOARD_SERVICES_HOME, 1, ""); // fill service spinner 1
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

*/
        jregisterservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = jservice.getText().toString();
                subservice = jsubservice.getText().toString();
                service_desc = jservdesc.getText().toString();
                min_price = jminprice.getText().toString();
                int foo_price = Integer.parseInt(min_price);
                address = jsaddress.getText().toString();
                city = jscity.getText().toString();
                pin = jpin.getText().toString();
                int foo_pin = Integer.parseInt(pin);
                web = jweb.getText().toString();

                /*

                if(service_desc.isEmpty())
                {
                    jservdesc.setError("Service Description Required!!");
                }
                    else if(service_desc.length() < 5)
                    {
                        jservdesc.setError("Service Description too short!!");
                    }
                        else if (min_price.isEmpty())
                        {
                            jminprice.setError("Please Provide Your Minimum Service Price!!");
                        }
                            else if (foo_price <= 99)
                            {
                                jminprice.setError("Minimum Service Price Should Be More Than 100 RS");
                            }
                                else if (address.isEmpty())
                                {
                                    jsaddress.setError("Please provide your address");
                                }
                                    else  if(address.length() < 7)
                                    {
                                      jsaddress.setError("Address very short, please enter proper address!!");
                                    }
                                        else  if(city.isEmpty())
                                        {
                                            jscity.setError("Please enter your servicing city");
                                        }
                                        else if(pin.isEmpty())
                                            {
                                                jpin.setError("Please provide your PIN-CODE");
                                            }
                                            else if(foo_pin < 2)
                                            {
                                                jpin.setError("Please provide correct PIN-CODE");

                                            }
                                                else if(Patterns.WEB_URL.matcher(web).matches())
                                                {
                                                    jweb.setError("Please provide your proper website");
                                                }
                                                else
                */
                register(userid, service, subservice, "banpic", "shoppic", "location", service_desc, min_price, address, city, pin, web, AppConfig.URL_ADD_SERVICE);
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
                            String a = jsonObject.getString("services");

                            list1.add(a);
                        }

                    } else if (spin == 2) {
                        JSONArray dash = response.getJSONArray("sub_service_list");

                        if (response.getString("result").equals("1")) {

                            JSONObject jsonObject;

                            for (int i = 0; i < dash.length(); i++) {

                                jsonObject = dash.getJSONObject(i);

                                String a = jsonObject.getString("sub_service_name");

                                list2.add(a);
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
                   // servicespinner.setAdapter(adapterONE);

                 //   fill_spinner(AppConfig.URL_SUB_SEVICES_HOME, 2, (String) servicespinner.getSelectedItem());//servicespinner.getSelectedItem().toString());
                    setAdapter(2);

                } else if (spinner == 2) {

                    adapterTWO = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list2);
                    adapterTWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 //   subservicespinner.setAdapter(adapterTWO);
                }
            }
        }, 2000);

    }

    private void register(String userid, String service, String subservice, String ban_pic, String shop_pic, String loc, String service_desc, String min_price, String address, String city, String pin, String web, String URL) {

//        pDialog.setMessage("Registering Service Provider......");
        //      showDialog();

        URL += "&userid=" + userid + "&add=" + address + "&ban_pic=" + ban_pic + "&website=" + web + "&shop_pic=" + shop_pic +
                "&loc=" + loc + "&s_name=" + service + "&s_price=" + min_price + "&s_sub_name=" + subservice +
                "&s_desc=" + service_desc + "&pin=" + pin;
        Toast.makeText(getActivity(), URL, Toast.LENGTH_SHORT).show();

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String res = response.getString("spid");
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}