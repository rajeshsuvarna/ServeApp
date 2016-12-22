package com.infouna.serveapp.activity;

/**
 * Created by Rajesh on 3/8/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
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
import com.infouna.serveapp.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String MyPREFERENCES = "MyPrefs.txt";
    public static final String user_id = "useridKey";
    public static final String type = "typeKey";
    public static final String spid = "spidKey";
    SharedPreferences sharedpreferences;

    @Bind(R.id.input_phone)
    EditText input_phone;

    private Button _loginButton;
    private Button _signupLink;
    private ProgressDialog pDialog;
    private SessionManager session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (Button) findViewById(R.id.btn_register);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UserRegistrationActivity.class);
                startActivity(i);

            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "inside click", Toast.LENGTH_LONG).show();

                String phone = input_phone.getText().toString().trim();

                if (phone.isEmpty() || phone.length() < 10 || phone.length() > 10) {
                    //Toast.makeText(getApplicationContext(), "Please registered phone number!", Toast.LENGTH_LONG).show();
                    input_phone.setError("Number not valid");
                } else {
                    input_phone.setError(null);
                    login(phone, AppConfig.URL_LOGIN);

                }
            }

            private void login(final String phone, String url) {

                pDialog.setIndeterminate(true);
                pDialog.setMessage("Authenticating...");
                showDialog();

                final String tag_json_obj = "json_obj_req";

                url += phone;

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String res = response.getString("result");
                                    if (res.equals("1")) {
                                        // Create login session
                                        session.setLogin(true);
                                        JSONObject jsonObject = response.getJSONObject("user_details");
                                        String res_user_id = jsonObject.getString("userid");
                                        String res_type = jsonObject.getString("user_type");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(user_id, res_user_id);
                                        editor.putString(type, res_type);

                                        if (res_type.equals("SP")) {
                                            String res_spid = jsonObject.getString("spid");

                                            editor.putString(spid, res_spid);
                                            editor.commit();
                                        }
                                        editor.commit();
                                        // hideDialog();

                                        String url_otp = AppConfig.LOGIN_SEND_OTP;
                                        url_otp += phone;

                                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_otp, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {

                                                            String res = response.getString("result");
                                                            if (res.equals("1")) {
                                                                hideDialog();
                                                                final String otp = response.getString("OTP");
                                                                final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(LoginActivity.this);
                                                                builder.setTitle("OTP : " + otp);
                                                                builder.setDescription("Sit back and relax while we verify your OTP");
                                                                builder.withDialogAnimation(true, Duration.SLOW);
                                                                builder.setStyle(Style.HEADER_WITH_TITLE);
                                                                builder.setNegativeText("Close");
                                                                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                        builder.autoDismiss(true);
                                                                    }
                                                                });
                                                                builder.show();


                                                                final Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        // Do something after 5s = 5000ms

                                                                        String url_send_otp = AppConfig.LOGIN_USER_VERIFY_OTP;
                                                                        url_send_otp += phone + "&otp=" + otp;

                                                                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_send_otp, null,
                                                                                new Response.Listener<JSONObject>() {
                                                                                    @Override
                                                                                    public void onResponse(JSONObject response) {
                                                                                        try {

                                                                                            String res = response.getString("result");

                                                                                            if (res.equals("1")) {
                                                                                                sucess();


                                                                                            } else {
                                                                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                                                                                                hideDialog();

                                                                                                hideDialog();
                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }

                                                                                    }
                                                                                }, new Response.ErrorListener() {

                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                                                                                hideDialog();

                                                                            }
                                                                        });

                                                                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                                                                    }
                                                                }, 3000);
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                                                                hideDialog();

                                                                hideDialog();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                                                hideDialog();

                                            }
                                        });

                                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                                    } else {
                                        input_phone.setError("Number not registered");
                                        // Toast.makeText(getApplicationContext(),"Number not Registered", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Unexpected Error, please try again later", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                        hideDialog();

                    }
                });

                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }

            private void sucess() {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
            }

            private void showDialog() {
                if (!pDialog.isShowing())
                    pDialog.show();
            }

            private void hideDialog() {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        });
    }
}