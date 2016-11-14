package com.infouna.serveapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class EditServiceProfileActivity extends AppCompatActivity {

    EditText add, web, loc, sname, ssname, sprice, tag, desc;
    String shop_pic, ban_pic;
    int i;
    ImageView iv_ban, iv_shop;
    Button update;
    private Toolbar toolbar;


    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String[] imgPath = new String[2];
    String[] fileName = new String[2];
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;

    public SharedPreferences spf;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Service Profile");

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add = (EditText) findViewById(R.id.ES_address);
        web = (EditText) findViewById(R.id.ES_web);
        loc = (EditText) findViewById(R.id.ES_loc);
        sname = (EditText) findViewById(R.id.ES_sname);
        ssname = (EditText) findViewById(R.id.ES_subservice);
        sprice = (EditText) findViewById(R.id.ES_price);
        tag = (EditText) findViewById(R.id.ES_tag);
        desc = (EditText) findViewById(R.id.ES_desc);

        SharedPreferences spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);

        add.setText(spf.getString("ES_address", ""));
        web.setText(spf.getString("ES_web", ""));
        loc.setText(spf.getString("ES_loc", ""));
        sname.setText(spf.getString("ES_sname", ""));
        ssname.setText(spf.getString("ES_subservice", ""));
        sprice.setText(spf.getString("ES_price", ""));
        tag.setText(spf.getString("ES_tag", ""));
        desc.setText(spf.getString("ES_desc", ""));

        iv_ban = (ImageView) findViewById(R.id.ES_ban_pic);
        iv_shop = (ImageView) findViewById(R.id.ES_shop_pic);

        update = (Button) findViewById(R.id.ES_update);

        iv_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;

                loadImagefromGallery();
            }
        });

        iv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;

                loadImagefromGallery();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath[i] = cursor.getString(columnIndex);
                cursor.close();

                if (i == 0) {
                    // Set the Image in ImageView
                    iv_shop.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath[i]));
                    // Get the Image's file name
                    String fileNameSegments[] = imgPath[i].split("/");
                    fileName[i] = fileNameSegments[fileNameSegments.length - 1];
                } else if (i == 1) {

                    // Set the Image in ImageView
                    iv_shop.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath[i]));
                    // Get the Image's file name
                    String fileNameSegments[] = imgPath[i].split("/");
                    fileName[i] = fileNameSegments[fileNameSegments.length - 1];
                }


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        for (int j = 0; j < 2; j++) {
            if (imgPath[j] != null && !imgPath[j].isEmpty()) {
                prgDialog.setMessage("Initiating...");
                prgDialog.show();
                // Convert image to String using Base64

                encodeImagetoString(j);


                // When Image is not selected from Gallery
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Please select an image",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString(final int step) {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath[step],
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Uploading...");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                params.put("filename", fileName[step]);

                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Uploading...");
        String tag = "params";
        Log.d(tag, params.toString());
        // Toast.makeText(getApplicationContext(), params.toString(),Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://serveapp.in/imgupload/upload_image.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                prgDialog.hide();
                Toast.makeText(getApplicationContext(), String.format("Image updated successfully"),
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(),
                            "Server not responding",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Device not connected to Internet. Try again", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void update_profile(final String sid, String add, String ban, String web, String shop, String loc, String sname,
                               String ss, String stitle, String tags, String price, String desc, String URL) {


        String tag_json_obj = "json_obj_req";

        URL += "&add=" + add + "&ban_pic=" + ban + "&website=" + web + "&shop_pic=" + shop + "&loc=" + loc +
                "&sid=" + sid + "&s_name=" + sname + "&ss_name=" + ss + "&s_title=" + stitle + "&tags=" + tag +
                "&s_price=" + price + "&s_desc=" + desc;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String res = response.toString();
                        finish();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EditServiceProfileActivity.this, "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();

            }
        });


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
