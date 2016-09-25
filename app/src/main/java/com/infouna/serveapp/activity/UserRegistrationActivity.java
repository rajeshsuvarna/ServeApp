package com.infouna.serveapp.activity;

/**
 * Created by MAHE on 3/9/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistration";
    public String result = "1";
    public String fname,lname,email,phone;

    JsonObjectRequest JsonObjectRequest;

    private EditText _fnameText;
    private EditText _lnameText;
    private EditText _emailText;
    private EditText _phoneNumber;
    private Button _signupButton;
    private Button _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        _fnameText = (EditText) findViewById(R.id.input_fname);
        _lnameText = (EditText) findViewById(R.id.input_lname);
        _emailText = (EditText) findViewById(R.id.input_email);
        _phoneNumber = (EditText) findViewById(R.id.input_reg_phone);
        _signupButton = (Button) findViewById(R.id.btn_reg);
        _loginLink = (Button) findViewById(R.id.btn_loginAcc);



        _loginLink = (Button) findViewById(R.id.btn_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "inside click", Toast.LENGTH_LONG).show();

                String fname = _fnameText.getText().toString().trim();
                String lname = _lnameText.getText().toString().trim();
                String email = _emailText.getText().toString().trim();
                String phone = _phoneNumber.getText().toString().trim();

                // Check for empty data in the form
                if (!fname.isEmpty() && !lname.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                    // login user
                    // Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_LONG).show();
                    String response = check_number(phone, AppConfig.URL_CHECK_NUMBER);
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    if(response.equals("0")) {
                        Toast.makeText(getApplicationContext(), "in if"+response, Toast.LENGTH_LONG).show();
                        user_register(fname, lname, email, phone, AppConfig.URL_REGISTER);

                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter all details!", Toast.LENGTH_LONG).show();
                }
            }

            private String check_number(String phone, String url) {
                String tag_json_obj = "json_obj_req";

                url += phone;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "Register Response: " + response.toString());
                                //hideDialog();
                                try {
                                    Toast.makeText(getApplicationContext(), "inside try", Toast.LENGTH_LONG).show();
                                    String res = response.getString("result");
                                    //  = response.getJSONArray("user_details");
                                    if (res.equals("0")) {
                                        //jsonObject.getString("userid");
                                        Toast.makeText(getApplicationContext(), "Number Verified", Toast.LENGTH_LONG).show();
                                        result = "0";

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Number already registered", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        //  hideDialog();

                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                return  result;
            }

            private void user_register(String fname, String lname, String email,String phone, String url) {
                String tag_json_obj = "json_obj_req";

                url += "&f_name=" + fname + "&l_name=" +lname+ "&email" +email+ "&mob" +phone;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "Register Response: " + response.toString());
                                //hideDialog();
                                try {
                                    Toast.makeText(getApplicationContext(), "inside try", Toast.LENGTH_LONG).show();
                                    String res = response.getString("result");
                                    String userid  = response.getString("userid");
                                    if (res.equals("1")) {
                                        //jsonObject.getString("userid");
                                        Toast.makeText(getApplicationContext(), userid.toString(), Toast.LENGTH_LONG).show();
                                        sucess();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        //  hideDialog();
                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }


            public void sucess() {
                Intent i =new Intent(UserRegistrationActivity.this,LoginActivity.class);
                startActivity(i);

            }

            public void onSignupFailed() {
                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

                _signupButton.setEnabled(true);
            }
        }
        );}
}

