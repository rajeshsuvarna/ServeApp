package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.infouna.serveapp.R;

/**
 * Created by Darshan on 03-10-2016.
 */
public class RequestNewServiceActivity extends Activity {

    EditText max, loc, date, add, desc;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_new_service);

        max = (EditText) findViewById(R.id.RNS_maxbudget);
        loc = (EditText) findViewById(R.id.RNS_location);
        date = (EditText) findViewById(R.id.RNS_date);
        add = (EditText) findViewById(R.id.RNS_add);
        desc = (EditText) findViewById(R.id.RNS_desc);

        confirm = (Button) findViewById(R.id.btn_confirm_req);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                Bundle b = i.getExtras();
                i.putExtra(b.getString("Hint_max"), max.getText().toString());
                i.putExtra(b.getString("Hint_loc"), loc.getText().toString());
                i.putExtra(b.getString("Hint_date"), date.getText().toString());
                i.putExtra(b.getString("Hint_add"), add.getText().toString());
                i.putExtra(b.getString("Hint_desc"), desc.getText().toString());
                setResult(1, i);
                finish();
            }
        });

    }
}
