package com.infouna.serveapp.activity;

/**
 * Created by Rajesh on 3/9/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import butterknife.Bind;
import butterknife.ButterKnife;


public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistration";
    public static String name, userid, finalUrl_reg;

    @Bind(R.id.input_fname)
    EditText _fnameText;
    @Bind(R.id.input_lname)
    EditText _lnameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_reg_phone)
    EditText _phoneNumber;

    private Button _signupButton;
    private Button _loginLink;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        ButterKnife.bind(this);

        _signupButton = (Button) findViewById(R.id.btn_reg);
        _loginLink = (Button) findViewById(R.id.btn_loginAcc);

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = _fnameText.getText().toString().trim();
                String lname = _lnameText.getText().toString().trim();
                String email = _emailText.getText().toString().trim();
                String phone = _phoneNumber.getText().toString().trim();
                name = fname + " " + lname;

                if (fname.isEmpty()) {
                    _fnameText.setError("Oops!! forgot your first name");
                } else if (fname.length() < 3) {
                    _fnameText.setError("Oh..very short name");
                } else if (lname.isEmpty()) {
                    _lnameText.setError("Oops forgot your surname");
                } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    _emailText.setError("Email address not valid");
                } else if (phone.isEmpty() || phone.length() < 10 || phone.length() > 10) {
                    _phoneNumber.setError("Number not valid");
                } else {
                    _phoneNumber.setError(null);
                    _emailText.setError(null);
                    _lnameText.setError(null);
                    _fnameText.setError(null);

                    user_register(fname, lname, email, phone, AppConfig.URL_REGISTER, AppConfig.URL_CHECK_NUMBER);

                }
            }

            private void user_register(final String fname, final String lname, final String email, final String phone, String url_reg, String url_check) {
                pDialog.setMessage("Validating Data......");
                showDialog();
                final String tag_json_obj = "json_obj_req";

                url_check += phone;
               // Toast.makeText(getApplicationContext(), "Url Check" + url_check.toString(), Toast.LENGTH_LONG).show();
                url_reg += "&f_name=" + fname + "&l_name=" + lname + "&email=" + email + "&mob=" + phone;

                finalUrl_reg = url_reg;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url_check, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String res = response.getString("result");

                                    if (res.equals("0")) {

                                        Toast.makeText(getApplicationContext(), "Number Verified", Toast.LENGTH_LONG).show();
                                        pDialog.setMessage("Registering ...");
                                        showDialog();
                                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, finalUrl_reg, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d(TAG, "Register Response: " + response.toString());

                                                       // Toast.makeText(getApplicationContext(), "in json", Toast.LENGTH_LONG).show();

                                                        try {
                                                            String res = response.getString("result");
                                                            userid = response.getString("userid");
                                                            if (res.equals("1")) {
                                                                hideDialog();
                                                                String url_otp = AppConfig.URL_SEND_OPT;
                                                                url_otp += userid;

                                                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url_otp, null,
                                                                        new Response.Listener<JSONObject>() {
                                                                            @Override
                                                                            public void onResponse(JSONObject response) {
                                                                                try {

                                                                                    String res = response.getString("result");
                                                                                    if (res.equals("1")) {
                                                                                        final String otp = response.getString("OTP");
                                                                                        final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(UserRegistrationActivity.this);
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

                                                                                                String url_send_otp = AppConfig.URL_OTP;
                                                                                                url_send_otp += userid + "&otp=" + otp;

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
                                                                hideDialog();

                                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {

                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                hideDialog();

                                                Toast.makeText(getApplicationContext(), "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();

                                            }
                                        });


                                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


                                    } else {
                                        hideDialog();
                                        _phoneNumber.setError("Number already registered");
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

                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }


            public void sucess() {

                final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(UserRegistrationActivity.this);
                builder.setTitle("Welcome to Serve App");
                builder.setDescription("Hello " + name + ", please login to continue... ");
                builder.withDialogAnimation(true, Duration.SLOW);
                builder.setStyle(Style.HEADER_WITH_TITLE);
                builder.setPositiveText("Proceed");
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                        startActivity(i);
                        builder.autoDismiss(true);
                    }
                });
                builder.show();


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

