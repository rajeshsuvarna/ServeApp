package com.infouna.serveapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Darshan on 14-04-2016.
 */

public class OrderDetailsSPActivity extends AppCompatActivity {

    TextView jmax, jloc, jdate, jdesc, jsname, jstatusdate, jstatustime, jacceptedstatus;
    Button jbtnaccept, jbtncancel, jbtnreport;
    ImageView jstatusicon;

    public String accepted, userid = "", spid = "", type = "", s_name = "";

    public Bundle b;

    Toolbar toolbar;

    private ProgressDialog pDialog;

    String uid, accepted_request_id,declined_request_id; // response from accept button click api call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_sp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Order Details");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        SharedPreferences spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");
        type = spf.getString("typeKey", "");
        spid = spf.getString("spidKey", "");

        Intent i = getIntent();
        b = i.getExtras();

        jmax = (TextView) findViewById(R.id.max_budget_ods);
        jloc = (TextView) findViewById(R.id.service_loc_ods);
        jdate = (TextView) findViewById(R.id.date_ods);
        jdesc = (TextView) findViewById(R.id.desc_ods);
        jsname = (TextView) findViewById(R.id.user_name_ods);
        jstatusdate = (TextView) findViewById(R.id.userdate_ods);
        jstatustime = (TextView) findViewById(R.id.usertime_ods);
        jacceptedstatus = (TextView) findViewById(R.id.status_ods);

        jbtnreport = (Button) findViewById(R.id.btn_report_customer_ods);
        jbtncancel = (Button) findViewById(R.id.btn_decline_ods);
        jbtnaccept = (Button) findViewById(R.id.btn_accept_ods);

        jstatusicon = (ImageView) findViewById(R.id.icon_ods);


        jbtnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accept_request(uid, b.getString("reqid"), AppConfig.ACCEPT_SERVICE_REQUEST);

            }
        });

        jbtnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetailsSPActivity.this, ReportServiceActivity.class);
                i.putExtra("userid", uid);    // pass values here
                i.putExtra("spid", spid);      // pass values here
                i.putExtra("reqid", b.getString("reqid"));
                i.putExtra("s_name", s_name);
                startActivity(i);
            }
        });

        jbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline_request(uid, b.getString("reqid"), AppConfig.DECLINE_SERVICE_REQUEST_SP); // userid, reqid, url
            }
        });

        load_order_details(spid, b.getString("reqid"), AppConfig.ORDER_DETAILS_SP); // spid, reqid, url as params

    }

    private void load_order_details(String sid, String rid, String url) {

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Loading Order Details...");
        showDialog();

        url += "&spid=" + sid + "&reqid=" + rid;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(OrderDetailsSPActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray dash = response.getJSONArray("sp_orders_details");

                            JSONObject jsonObject = dash.getJSONObject(0);

                            s_name = jsonObject.getString("username");

                            jmax.setText(jsonObject.getString("max_budget"));
                            jloc.setText(jsonObject.getString("location"));
                            jdate.setText(jsonObject.getString("reqstd_date_time"));
                            jdesc.setText(jsonObject.getString("desc"));
                            String accepted = jsonObject.getString("accepted");

                            uid = jsonObject.getString("userid");

                            if (accepted.equals("1")) {
                                jstatusicon.setImageResource(R.mipmap.ic_check);
                                jacceptedstatus.setText("Request Accepted");
                            } else if (accepted.equals("0")) {
                                jstatusicon.setImageResource(R.mipmap.ic_warning_notification);
                                jacceptedstatus.setText("Order Declined");
                            } else {
                                jstatusicon.setImageResource(R.mipmap.ic_warning_notification);
                                jacceptedstatus.setText("Pending Your Approval");
                            }

                            String dt = jsonObject.getString("reqstd_date_time");

                            String[] split = dt.split(" ");

                            jdate.setText(split[0]);

                            jsname.setText(s_name);

                            jstatusdate.setText(split[0]);
                           // jstatustime.setText(split[1]);
                            hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void accept_request(String uid, String reqid, String url) {

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Accepting Order...");
        showDialog();


        url += "&userid=" + uid + "&reqid=" + reqid + "&req=1";

       // Toast.makeText(OrderDetailsSPActivity.this, url, Toast.LENGTH_SHORT).show();

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            accepted_request_id = response.getString("reqid").toString();
                            Toast.makeText(OrderDetailsSPActivity.this, "Request accepted", Toast.LENGTH_SHORT).show();
                            load_order_details(spid,accepted_request_id, AppConfig.ORDER_DETAILS_SP);
                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void decline_request(String uid, String reqid, String url) {

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Declining Your Order...");
        showDialog();

        url += "&userid=" + uid + "&reqid=" + reqid + "&req=0";

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            accepted_request_id = response.getString("reqid").toString();
                            Toast.makeText(OrderDetailsSPActivity.this, "Request Declined", Toast.LENGTH_SHORT).show();
                            load_order_details(spid,accepted_request_id, AppConfig.ORDER_DETAILS_SP);
                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
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