package com.infouna.serveapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.fragments.MyServiceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * * Created by Darshan on 15-04-2016.
 **/

public class OrderDetailsUserActivity extends AppCompatActivity {

    TextView jmax, jloc, jdate, jdesc, jsname, jstatusdate, jstatustime, jacceptedstatus;
    Button jbtnreport, jbtncancel, jbtnrate;
    ImageView jstatusicon;

    Toolbar toolbar;

    private ProgressDialog pDialog;

    String accepted = "", reqid = "", userid = "", sname = "", spid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_user);

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

        SharedPreferences spf = getSharedPreferences("MyPrefs.txt", MODE_PRIVATE);
        String type = spf.getString("typeKey", "");
        userid = spf.getString("useridKey", "");
        reqid = spf.getString("reqidKey", "");
       // sname = spf.getString("r_snameKey","");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Loading Order Details...");
        showDialog();

        // Toast.makeText(OrderDetailsUserActivity.this, type, Toast.LENGTH_SHORT).show();
        if (type.equals("SP")) {
            spid = spf.getString("spidKey", "");
        }

        jmax = (TextView) findViewById(R.id.max_budget);
        jloc = (TextView) findViewById(R.id.textview_service_location);
        jdate = (TextView) findViewById(R.id.textview_service_date);
        jdesc = (TextView) findViewById(R.id.textview_add_desc);
        jsname = (TextView) findViewById(R.id.service_name_odu);
        jstatusdate = (TextView) findViewById(R.id.service_date_odu);
      //  jstatustime = (TextView) findViewById(R.id.service_time_odu);
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
                            // Toast.makeText(OrderDetailsUserActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                            JSONArray dash = response.getJSONArray("orders_details");

                            //Toast.makeText(OrderDetailsUserActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject = dash.getJSONObject(0);

                            sname = jsonObject.getString("service_name");
                            spid = jsonObject.getString("spid");
                            jmax.setText(jsonObject.getString("max_budget"));
                            jloc.setText(jsonObject.getString("location"));
                            jdesc.setText(jsonObject.getString("description"));
                            jsname.setText(sname);
                            String[] dt = jsonObject.getString("reqested_date_time").split(" ");
                            jstatusdate.setText(dt[0]);
                          //  jstatustime.setText(dt[1]);

                            jdate.setText(dt[0]);

                            accepted = jsonObject.getString("accepted").trim();

                            if (accepted.equals("1")) {
                                jacceptedstatus.setText("Accepted");
                                jstatusicon.setImageResource(R.mipmap.ic_check);
                            } else if (accepted.equals("0")) {
                                jacceptedstatus.setText("Declined");
                                jstatusicon.setImageResource(R.mipmap.ic_warning_notification);
                            } else {
                                jacceptedstatus.setText("Pending Approval from Service Provider");
                                jstatusicon.setImageResource(R.mipmap.ic_warning_notification);
                            }

                            //Toast.makeText(OrderDetailsUserActivity.this, "", Toast.LENGTH_SHORT).show();
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

    public void cancel_request(String uid, String spid, String reqid, String url) {

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Cancelling Your Order...");
        showDialog();

        url += "&userid=" + uid + "&spid=" + spid + "&reqid=" + reqid;

        String tag_json_obj = "json_obj_req";

      //  Toast.makeText(OrderDetailsUserActivity.this, url, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Toast.makeText(OrderDetailsUserActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                        final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(OrderDetailsUserActivity.this);
                        builder.setTitle("You cancelled this service");
                        builder.setDescription("Please view other services in home... ");
                        builder.withDialogAnimation(true, Duration.SLOW);
                        builder.setStyle(Style.HEADER_WITH_TITLE);
                        builder.setPositiveText("OK");
                        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent i = new Intent(OrderDetailsUserActivity.this, HomeActivity.class);
                                startActivity(i);
                                builder.autoDismiss(true);
                            }
                        });
                        builder.show();
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
        Intent back = new Intent(this, HomeActivity.class);
        startActivity(back);
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