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
import android.widget.TextView;
import android.widget.Toast;

import com.infouna.serveapp.R;


import butterknife.Bind;
import  butterknife.OnClick;
import butterknife.ButterKnife;



public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText inputPhone;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputPhone=(EditText) findViewById(R.id.input_phone);
        loginButton = (Button) findViewById(R.id.btn_login);
        registerButton=(Button) findViewById(R.id.btn_register);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, UserRegistration.class);
                startActivity(i);
            }
        });
    }
}
