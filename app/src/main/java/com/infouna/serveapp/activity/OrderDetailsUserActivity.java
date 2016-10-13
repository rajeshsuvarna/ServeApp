package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

/**
 * Created by Darshan on 15-04-2016.
 */
public class OrderDetailsUserActivity extends Activity {

    TextView jmax, jloc, jdate, jdesc, jsname, jstatusdate, jstatustime, jacceptedstatus;
    Button jbtnreport, jbtncancel, jbtnrate;
    ImageView jstatusicon;

    String accepted = "", reqid = "", userid = "", sname = "", spid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_user);

        Bundle b = getIntent().getExtras();
        userid = b.getString("userid");
        reqid = b.getString("reqid");

        SharedPreferences spf = getSharedPreferences("MyPrefs.txt", MODE_PRIVATE);
        String type = spf.getString("typeKey", "");
        Toast.makeText(OrderDetailsUserActivity.this, type, Toast.LENGTH_SHORT).show();
        if (type.equals("SP")) {
            spid = spf.getString("spidKey", "");
        }

        jmax = (TextView) findViewById(R.id.max_budget);
        jloc = (TextView) findViewById(R.id.textview_service_location);
        jdate = (TextView) findViewById(R.id.textview_service_date);
        jdesc = (TextView) findViewById(R.id.textview_add_desc);
        jsname = (TextView) findViewById(R.id.service_name_odu);
        jstatusdate = (TextView) findViewById(R.id.service_date_odu);
        jstatustime = (TextView) findViewById(R.id.service_time_odu);
        jacceptedstatus = (TextView) findViewById(R.id.accepted_status);

        jbtnreport = (Button) findViewById(R.id.report);
        jbtncancel = (Button) findViewById(R.id.cancel);
        jbtnrate = (Button) findViewById(R.id.rate);

        jstatusicon = (ImageView) findViewById(R.id.icon_odu);

        jbtnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetailsUserActivity.this, ReportServiceActivity.class);
                i.putExtra("userid", userid);    // pass values here
                i.putExtra("spid", spid);      // pass values here
                i.putExtra("reqid", reqid);
                i.putExtra("s_name", sname);
                startActivity(i);
            }
        });

        jbtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_request(userid, spid, reqid, AppConfig.CANCEL_SERVICE_REQUEST);
            }
        });

        jbtnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetailsUserActivity.this, RateServiceActivity.class);
                i.putExtra("userid", userid);    // pass values here
                i.putExtra("spid", spid);      // pass values here
                i.putExtra("reqid", reqid);
                i.putExtra("s_name", sname);
                startActivity(i);
            }
        });

        load_order_details(userid, reqid, AppConfig.ORDER_DETAILS_USER);
        if (accepted.equals("1")) {
            jacceptedstatus.setText("Accepted");
            jstatusicon.setImageResource(R.mipmap.ic_check);
        } else if (accepted.equals("0")) {
            jacceptedstatus.setText("Pending Approval from Service Provider");
            jstatusicon.setImageResource(R.mipmap.ic_warning_notification);
        }

    }

    private void load_order_details(String uid, String rid, String url) {

        url += "&userid=" + uid + "&reqid=" + rid;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray dash = response.getJSONArray("orders_details");

                            JSONObject jsonObject = dash.getJSONObject(0);

                            sname = jsonObject.getString("service_name");

                            jmax.setText(jsonObject.getString("max_budget"));
                            jloc.setText(jsonObject.getString("location"));
                            jdate.setText(jsonObject.getString("reqested_date_time"));
                            jdesc.setText(jsonObject.getString("description"));
                            jsname.setText(sname);
                            jstatusdate.setText(jsonObject.getString("requested_date_time"));
                            jstatustime.setText(jsonObject.getString("requested_date_time"));

                            accepted = jsonObject.getString("accepted");

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

    public void cancel_request(String uid, String spid, String reqid, String url) {

        url += "&userid=" + uid + "&spid=" + spid + "&reqid=" + reqid;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(OrderDetailsUserActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
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