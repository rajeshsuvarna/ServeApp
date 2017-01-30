package com.infouna.serveapp.activity;

/**
 * Created by Rajesh on 3/8/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;


public class LoginActivity extends AppCompatActivity implements OTPListener {
    private static final String TAG = "LoginActivity";
    public static final String MyPREFERENCES = "MyPrefs.txt";
    public static final String user_id = "useridKey";
    public static final String type = "typeKey";
    public static final String spid = "spidKey";

    public  String fOTP;
    public  String otp;
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
        OtpReader.bind(LoginActivity.this,"SerApp");
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
                                                                otp = response.getString("OTP");

                                                                        pDialog.setIndeterminate(true);
                                                                        pDialog.setMessage("Detecting your OTP...");
                                                                        showDialog();




                                                                final Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                         //   hideDialog();
                                                                            // Do something after 5s = 5000ms
                                                                            try {
                                                                                boolean vOTP = vOTP(fOTP, otp);

                                                                                if (vOTP) {

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
                                                                                } else

                                                                                {
                                                                                    hideDialog();
                                                                                    Toast.makeText(getApplicationContext(), "OTP Not received , please enter correct number or try after some time", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                            catch (Exception e)
                                                                            {
                                                                                hideDialog();
                                                                                Toast.makeText(getApplicationContext(), "OTP not received, please enter correct number", Toast.LENGTH_LONG).show();
                                                                            }
                                                                    }
                                                                    }, 20000);

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






        });
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
    public void otpReceived(String messageText) {
       // Toast.makeText(this, "Got " + messageText, Toast.LENGTH_LONG).show();

        hideDialog();
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Verifying your OTP...");
        showDialog();
        Log.d("Otp", messageText);

        String[] nbs = messageText.split("\\D+");
        if (nbs.length != 0) {
            for (String number : nbs) {
                if (number.matches("^[0-9]+$")) {
                    fOTP = number;
                }
            }
        }


        final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(LoginActivity.this);
        builder.setTitle("OTP : " + fOTP);
        builder.setDescription("Your Serve App OTP has received. Auto-Verifying the OTP to complete login.");
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
    }

    private boolean vOTP(String messageText, String motp) {
        final String mTXT = messageText;
        final String mOTP = motp;
       // Toast.makeText(this, "verify " + messageText + motp, Toast.LENGTH_LONG).show();

        if (mTXT.equalsIgnoreCase(mOTP))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}