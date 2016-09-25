package com.infouna.serveapp.activity;

/**
 * Created by MAHE on 3/8/2016.
 */

import android.app.ProgressDialog;
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
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.OrderListCardSP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    JsonObjectRequest jsonObjReq;


    public String url = AppConfig.URL_LOGIN, num = "";

    @Bind(R.id.input_phone)
    EditText input_phone;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.btn_register)
    Button _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                num = input_phone.getText().toString();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), UserRegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    public void login() {

        url += num;

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                  //  JSONObject dash = response.getJSONObject("user_details");
                    Toast.makeText(LoginActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

                  //  if (response.getString("result").equals("1")) {

//                        Toast.makeText(LoginActivity.this, dash.getString("userid"), Toast.LENGTH_SHORT).show();
  //                  }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


}
