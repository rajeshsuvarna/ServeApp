package com.infouna.serveapp.activity;

/**
 * Created by Rajesh on 3/8/2016.
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity.txt";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String user_id = "useridKey";
    public static final String type = "typeKey";
    public SharedPreferences sharedpreferences;

    @Bind(R.id.input_phone)
    EditText input_phone;

    private Button _loginButton;
    private Button _signupLink;
    private ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (Button) findViewById(R.id.btn_register);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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

            private void login(String phone, String url) {

                pDialog.setIndeterminate(true);
                pDialog.setMessage("Authenticating...");
                showDialog();

                String tag_json_obj = "json_obj_req";

                url += phone;

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String res = response.getString("result");
                                    if (res.equals("1")) {

                                        JSONObject jsonObject = response.getJSONObject("user_details");
                                        String res_user_id = jsonObject.getString("userid");
                                        String res_type = jsonObject.getString("user_type");
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(user_id, res_user_id);
                                        editor.putString(type, res_type);
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), res_user_id, Toast.LENGTH_LONG).show();
                                        hideDialog();
                                        sucess();
                                    } else {
                                        input_phone.setError("Number not registered");
                                        // Toast.makeText(getApplicationContext(),"Number not Registered", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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