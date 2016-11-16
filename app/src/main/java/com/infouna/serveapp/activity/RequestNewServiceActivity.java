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

/**
 * Created by Darshan on 03-10-2016.
 */
public class RequestNewServiceActivity extends AppCompatActivity {

    EditText max, loc, date, address, desc;
    Button confirm;
    Toolbar toolbar;
    String userid, sp_id, s_name, s_sub_name, s_max_budget, s_location, s_req_dt, s_address, s_description,s_title;
    public static SharedPreferences spf;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_service);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Service Details");

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

        max = (EditText) findViewById(R.id.RNS_maxbudget);
        loc = (EditText) findViewById(R.id.RNS_location);
        date = (EditText) findViewById(R.id.RNS_date);
        address = (EditText) findViewById(R.id.RNS_add);
        desc = (EditText) findViewById(R.id.RNS_desc);

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
                 s_max_budget = max.getText().toString();
                 s_location = loc.getText().toString();
                 s_req_dt = date.getText().toString();
                 s_address = address.getText().toString();
                 s_description = desc.getText().toString();
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Booking...");
                showDialog();


                request_service(userid,sp_id,s_name,s_sub_name,s_max_budget,s_location,s_req_dt,s_address,s_description,s_title, AppConfig.SERVICE_REQUEST);

               // finish();
            }
        });

    }

    private void request_service(String userid, String s, String s_name, String s_sub_name, String max_budget, String location, String req_dt, String addr, String desc, String title, String URL) {
        URL += "&userid=" + userid + "&spid=" + s + "&s_name=" + s_name + "&s_sub_name=" + s_sub_name + "&max_budget=" + max_budget + "&location="
                + location + "&req_dt=" + req_dt + "&add=" + addr + "&desc=" + desc + "&s_title=" + title ;

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

                                /*
                                final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getApplicationContext());
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
                                builder.show();*/
                            }
                            else {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();

                              /*  final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getApplicationContext());
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
                                builder.show();*/

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
