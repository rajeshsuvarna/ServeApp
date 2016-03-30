package com.infouna.serveapp.activity;

/**
 * Created by MAHE on 3/9/2016.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

import com.infouna.serveapp.R;


import butterknife.Bind;
import  butterknife.OnClick;
public class UserRegistration extends AppCompatActivity{

    private static final String TAG = "UserRegistration";

    @Bind(R.id.input_fname) EditText _fnameText;
    @Bind(R.id.input_lname) EditText _lnameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_reg_phone) EditText _phoneNumber;
    @Bind(R.id.btn_reg) Button _signupButton;
    @Bind(R.id.btn_loginAcc) Button _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(UserRegistration.this,
                R.style.MyMaterialTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phoneNumber.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phoneNumber.getText().toString();

        if (fname.isEmpty()) {
            _fnameText.setError("Oops forgot your first name");
            valid = false;
        } else if(fname.length() < 3) {
            _fnameText.setError("Oh..very short name");
            valid = false;
        }
        else
        {
            _fnameText.setError(null);
        }

        if (lname.isEmpty()) {
            _lnameText.setError("Oops forgot your surname");
            valid = false;
        }
        else
        {
            _lnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Email address not valid");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10 || phone.length() > 10) {
            _phoneNumber.setError("Number not valid");
            valid = false;
        } else {
            _phoneNumber.setError(null);
        }

        return valid;
    }


}
