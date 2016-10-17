package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by Darshan on 31-03-2016.
 */
public class RateServiceActivity extends AppCompatActivity {

    ImageButton stars[] = new ImageButton[5];

    Toolbar toolbar;


    int rate;

    EditText jreview;
    Button jsubmitreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_service);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Rate Service");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        stars[0] = (ImageButton) findViewById(R.id.star_1);
        stars[1] = (ImageButton) findViewById(R.id.star_2);
        stars[2] = (ImageButton) findViewById(R.id.star_3);
        stars[3] = (ImageButton) findViewById(R.id.star_4);
        stars[4] = (ImageButton) findViewById(R.id.star_5);

        jreview = (EditText) findViewById(R.id.review);
        jsubmitreview = (Button) findViewById(R.id.submit_review);

        jsubmitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitreview("566912", "12345", "carpentor", rate, jreview.getText().toString()); // passing parameters of url to function; refer URL documentation

            }
        });

    }

    private void submitreview(String uid, String spid, String sname, int rating, String review) {

        String tag_json_obj = "json_obj_req";

        String url = AppConfig.URL_RATE_SERVICE;

        url += "&userid=" + uid + "&spid=" + spid + "&s_name=" + sname + "&rating=" + String.valueOf(rating) + "&reviews=" + review;

        if (review.length() != 0 && rate != 0) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(RateServiceActivity.this, response.toString(), Toast.LENGTH_SHORT).show(); // replace with assignment statement
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


// Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } else {

            Toast.makeText(RateServiceActivity.this, "Ratings and comments necessary", Toast.LENGTH_SHORT).show();

        }


    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.star_1:
                rater(1);
                break;
            case R.id.star_2:
                rater(2);
                break;
            case R.id.star_3:
                rater(3);
                break;
            case R.id.star_4:
                rater(4);
                break;
            case R.id.star_5:
                rater(5);
                break;
        }

    }

    public void rater(int rating) {
        rate = rating;
        clear_rating();
        for (int i = 0; i < rating; i++)
            stars[i].setImageResource(R.mipmap.ic_star_selected);
    }

    public void clear_rating() {
        for (int i = 0; i < 5; i++)
            stars[i].setImageResource(R.mipmap.ic_star_deselected);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,OrderDetailsUserActivity.class);
        startActivity(back);
        finish();
    }
}