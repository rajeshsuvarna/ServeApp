package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONObject;

/**
 * Created by Darshan on 31-03-2016.
 */
public class ReportServiceActivity extends AppCompatActivity {

    Bundle b;

    Toolbar toolbar;

    EditText jreportcomment;
    Button jbtnsubmit;

    String userid, spid, reqid, sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_service);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Report Service");

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


        Intent i = getIntent();
        b = i.getExtras();

        userid = b.getString("userid");
        spid = b.getString("spid");
        reqid = b.getString("reqid");

        jreportcomment = (EditText) findViewById(R.id.reportcomment);
        jbtnsubmit = (Button) findViewById(R.id.submitreport);

        jbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                report(userid, spid, reqid, sname, jreportcomment.getText().toString());

            }
        });

    }

    private void report(String uid, String spid, String reqid, String s_name, String comments) {

        String tag_json_obj = "json_obj_req";

        String url = AppConfig.URL_REPORT_SERVICE;

        url += "&userid=" + uid + "&spid=" + spid + "&reqid=" + reqid + "&s_name=" + s_name + "&comments=" + comments;

        if (jreportcomment.length() != 0) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


// Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } else {

            Toast.makeText(ReportServiceActivity.this, " Comment cannot be blank ", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onBackPressed() {
        //  Intent back = new Intent(this,OrderDetailsUserActivity.class);
        // startActivity(back);
        finish();
    }
}
