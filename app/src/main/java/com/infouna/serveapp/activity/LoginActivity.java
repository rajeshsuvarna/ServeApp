package com.infouna.serveapp.activity;

/**
 * Created by MAHE on 3/8/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.helper.SQLiteHandler;
import com.infouna.serveapp.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity.txt";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String user_id = "useridKey";
    public static  final String type = "typeKey";
    SharedPreferences sharedpreferences;


    private EditText input_phone;
    private Button _loginButton;
    private Button _signupLink;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        input_phone = (EditText) findViewById(R.id.input_phone);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (Button) findViewById(R.id.btn_register);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,UserRegistrationActivity.class);
                startActivity(i);

            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "inside click", Toast.LENGTH_LONG).show();

                String phone = input_phone.getText().toString().trim();

                // Check for empty data in the form
                if (!phone.isEmpty()) {
                    // login user
                   // Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_LONG).show();
                    login(phone, AppConfig.URL_LOGIN);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please registered phone number!", Toast.LENGTH_LONG).show();
                }
            }


            private void login(String phone, String url) {
                // Tag used to cancel the request
                //String tag_string_req = "req_login";

                //pDialog.setMessage("Logging in ...");
                //showDialog();

                String tag_json_obj = "json_obj_req";

                url += phone;


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, "Login Response: " + response.toString());
                                //hideDialog();
                                try {
                                    Toast.makeText(getApplicationContext(), "inside try", Toast.LENGTH_LONG).show();


                                    String res = response.getString("result");
                                   //  = response.getJSONArray("user_details");

                                    if (res.equals("1")) {
                                        JSONObject jsonObject = response.getJSONObject("user_details");

                                        //jsonObject.getString("userid");

                                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                                        String res_user_id =  jsonObject.getString("userid");
                                        String res_type =  jsonObject.getString("user_type");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(user_id, res_user_id);
                                        editor.putString(type, res_type);
                                        editor.commit();

                                        sucess();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Number not Registered", Toast.LENGTH_LONG).show();
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

            private  void  sucess()
            {
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
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