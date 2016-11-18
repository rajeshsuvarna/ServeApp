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
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Darshan on 03-10-2016.
 */
public class RequestNewServiceActivity extends AppCompatActivity {

   // EditText max, loc, date, address, desc;
    Button confirm;
    Toolbar toolbar;
    String userid, sp_id, s_name, s_sub_name, s_max_budget, s_location, s_req_dt, s_address, s_description,s_title;
    public static SharedPreferences spf;
    private ProgressDialog pDialog;

    @Bind(R.id.RNS_maxbudget)
    EditText max;
    @Bind(R.id.RNS_location)
    EditText loc;
    @Bind(R.id.RNS_date)
    EditText date;
    @Bind(R.id.RNS_add)
    EditText address;
    @Bind(R.id.RNS_desc)
    EditText desc;

    String[] encodedParams = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_service);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Service Request");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*max = (EditText) findViewById(R.id.RNS_maxbudget);
        loc = (EditText) findViewById(R.id.RNS_location);
        date = (EditText) findViewById(R.id.RNS_date);
        address = (EditText) findViewById(R.id.RNS_add);
        desc = (EditText) findViewById(R.id.RNS_desc);*/

        confirm = (Button) findViewById(R.id.btn_confirm_req);


        spf = this.getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "Null String");
        sp_id = spf.getString("service_id_Key", "Null String");
        s_name = spf.getString("service_name_key", "Null String");
        s_sub_name = spf.getString("sub_service_name_key", "Null");
        s_title = spf.getString("service_title_key", "Null");


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 s_max_budget = max.getText().toString().trim();
                 s_location = loc.getText().toString().trim();
                 s_req_dt = date.getText().toString().trim();
                 s_address = address.getText().toString().trim();
                 s_description = desc.getText().toString().trim();

                if (s_max_budget.isEmpty()) {
                    max.setError("Maximum budget needed");
                } else if (s_location.isEmpty()) {
                    loc.setError("Service location needed");
                } else if (s_req_dt.isEmpty()) {
                    date.setError("Service date needed");
                } else if (s_address.isEmpty()) {
                    address.setError("Address for service needed");
                } else if (s_description.isEmpty()) {
                    desc.setError("Description needed");
                } else {
                    max.setError(null);
                    loc.setError(null);
                    date.setError(null);
                    address.setError(null);
                    desc.setError(null);

                pDialog.setIndeterminate(true);
                pDialog.setMessage("Booking...");
                showDialog();


                    try {
                        request_service(userid,sp_id,s_name,s_sub_name,s_max_budget,s_location,s_req_dt,s_address,s_description,s_title, AppConfig.SERVICE_REQUEST);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

               // finish();
            }
        });

    }

    private void request_service(String userid, String s, String s_name, String s_sub_name, String max_budget, String location, String req_dt, String addr, String desc, String title, String URL) throws UnsupportedEncodingException {

        encodedParams[0] = URLEncoder.encode(max_budget,"utf-8");
        encodedParams[1] = URLEncoder.encode(location,"utf-8");
        encodedParams[2] = URLEncoder.encode(addr,"utf-8");
        encodedParams[3] = URLEncoder.encode(req_dt,"utf-8");
        encodedParams[4] = URLEncoder.encode(desc,"utf-8");

        URL += "&userid=" + userid + "&spid=" + s + "&s_name=" + s_name + "&s_sub_name=" + s_sub_name + "&max_budget=" + encodedParams[0] + "&location="
                + encodedParams[1] + "&req_dt=" + encodedParams[3] + "&add=" + encodedParams[2] + "&desc=" + encodedParams[4] + "&s_title=" + title ;

        String tag_json_obj = "json_obj_req";

       // Toast.makeText(getApplicationContext(), URL, Toast.LENGTH_LONG).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                           // String reqid = response.getString("reqid");
                           // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            String res = response.getString("result");



                            if (res.equals("1")) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Service Booked, we will notify once provider has confirmed your order", Toast.LENGTH_LONG).show();


                                final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(RequestNewServiceActivity.this);
                                builder.setTitle("Service Booked");
                                builder.setDescription("We will notify you when the service is accepted from the provider ...");
                                builder.withDialogAnimation(true, Duration.SLOW);
                                builder.setStyle(Style.HEADER_WITH_TITLE);
                                builder.setPositiveText("OK");
                                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(i);
                                        builder.autoDismiss(true);
                                    }
                                });
                                builder.show();
                            }
                            else {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();

                                final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(RequestNewServiceActivity.this);
                                builder.setTitle("SORRY");
                                builder.setDescription("Cannot book now..Come back later");
                                builder.withDialogAnimation(true, Duration.SLOW);
                                builder.setStyle(Style.HEADER_WITH_TITLE);
                                builder.setPositiveText("OK");
                                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(i);
                                        builder.autoDismiss(true);
                                    }
                                });
                                builder.show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,ServiceDetailsActivity.class);
        startActivity(back);
        finish();
    }
}
