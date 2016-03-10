package com.infouna.serveapp.activity;

/**
 * Created by MAHE on 3/9/2016.
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
public class UserRegistration extends AppCompatActivity{

    private static final String TAG = "RegisterActivity";
    private static final int REQUEST_REGISTER = 0;
    EditText inputFname,inputLname,inputEmail,inputMobile;
    Button loginButton,registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        inputEmail=(EditText) findViewById(R.id.input_email);
        inputFname=(EditText) findViewById(R.id.input_fname);
        inputLname=(EditText) findViewById(R.id.input_lname);
        inputMobile=(EditText) findViewById(R.id.input_reg_phone);
        loginButton=(Button) findViewById(R.id.btn_loginAcc);
        registerButton=(Button) findViewById(R.id.btn_reg);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRegistration.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }



}
