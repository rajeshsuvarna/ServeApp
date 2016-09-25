package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Darshan on 13-04-2016.
 */
public class ServiceDetailsActivity extends Activity {

    TextView servicename, review_count, address;
    Button request;
    ImageButton btnfav, stars[] = new ImageButton[5];
    LinearLayout background;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    String fav, ratin;
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        servicename = (TextView) findViewById(R.id.sd_sname);
        review_count = (TextView) findViewById(R.id.sd_reviewcount);
        address = (TextView) findViewById(R.id.sd_address);
        background = (LinearLayout) findViewById(R.id.sd_background);

        request = (Button) findViewById(R.id.btn_sd_request);

        btnfav = (ImageButton) findViewById(R.id.sd_favourite);

        stars[0] = (ImageButton) findViewById(R.id.star_1);
        stars[1] = (ImageButton) findViewById(R.id.star_2);
        stars[2] = (ImageButton) findViewById(R.id.star_3);
        stars[3] = (ImageButton) findViewById(R.id.star_4);
        stars[4] = (ImageButton) findViewById(R.id.star_5);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // requestService();
            }
        });

        load_order_details(b.getString("uid"), b.getString("servicename"), b.getString("fav"), AppConfig.SERVICE_DETAILS_URL);
        loadImages(b.getString("picture"));
    }

    private void load_order_details(String uid, final String sname, final String fav, String url) {

        url += "&userid=" + uid + "&s_name=" + sname;

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray dash = response.getJSONArray("service_details");

                            JSONObject jsonObject = dash.getJSONObject(0);

                            String reviews = jsonObject.getString("total_reviews");

                            servicename.setText(sname);

                            if (Integer.parseInt(reviews) > 0) {
                                review_count.setText(reviews + "reviews");
                            } else {
                                review_count.setText("0 reviews");
                            }

                            address.setText(jsonObject.getString("location"));

                            rater(Integer.parseInt(jsonObject.getString("total_ratings")));

                            if (fav.equals("1")) {
                                btnfav.setImageResource(R.mipmap.ic_like_selected);
                            } else {
                                btnfav.setImageResource(R.mipmap.ic_like_deselected);
                            }

                        } catch (JSONException e) {
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
    }

    public void rater(int rating) {

        clear_rating();
        for (int i = 0; i < rating; i++)
            stars[i].setImageResource(R.mipmap.ic_star_selected);
    }

    public void clear_rating() {
        for (int i = 0; i < 5; i++)
            stars[i].setImageResource(R.mipmap.ic_star_deselected);
    }

    private void loadImages(String urlThumbnail) {

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    background.setBackground(new BitmapDrawable(response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

}