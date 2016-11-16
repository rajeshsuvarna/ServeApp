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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.fragments.HomeFragment;
import com.infouna.serveapp.fragments.UserProfile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Darshan on 04-11-2016.
 */

public class EditUserProfileActivity extends AppCompatActivity {

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    public static final String profile = "profilepicKey";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    String profile_pic;

    EditText fname, lname, mob, e_mail;
    String userid = "", f, l, m, e;
    ImageView iv;
    Button update;

    public SharedPreferences spf;
    private Toolbar toolbar;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        verifyStoragePermissions(EditUserProfileActivity.this);

        SharedPreferences spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit User Profile");

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

        fname = (EditText) findViewById(R.id.EU_fname);
        lname = (EditText) findViewById(R.id.EU_lname);
        mob = (EditText) findViewById(R.id.EU_mob);
        e_mail = (EditText) findViewById(R.id.EU_email);

        iv = (ImageView) findViewById(R.id.EU_profilepic);

        update = (Button) findViewById(R.id.EU_update);

        spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);

        fname.setText(spf.getString("fnameKey", "Null String"));
        lname.setText(spf.getString("lnameKey", "Null String"));
        e_mail.setText(spf.getString("emailKey", "Null String"));
        mob.setText(spf.getString("phoneKey", "Null String"));

        profile_pic = spf.getString(profile, "Null String");

        if (profile_pic.contains("serveapp")) {
            loadImages(profile_pic);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImagefromGallery();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                f = fname.getText().toString();
                l = lname.getText().toString();
                e = e_mail.getText().toString();
                m = mob.getText().toString();

                uploadImage(v);

            }
        });
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        iv.setImageDrawable(null);
    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView
                iv.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
              //  Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage(View v) {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Initiating...");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();

            update_profile(userid, f, l, e, m, fileName, AppConfig.URL_UPDATE_USER_PROFILE);  // update profile

            // When Image is not selected from Gallery
        } else {
            Toast.makeText(getApplicationContext(),   "Please select an image", Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
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
                params.put("filename", fileName);

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
                Toast.makeText(getApplicationContext(), String.format("Image updated successfully"),Toast.LENGTH_LONG).show();
                //showConfirmDialog();


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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

   /* public void  showConfirmDialog(){
        final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getApplicationContext());
        builder.setTitle("Oops sorry...");
        builder.setDescription("NO Notifications yet...");
        builder.withDialogAnimation(true, Duration.SLOW);
        builder.setStyle(Style.HEADER_WITH_TITLE);
        builder.setPositiveText("OK");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                builder.autoDismiss(true);
            }
        });
        builder.show();
    }
*/
    private void loadImages(String urlThumbnail) {
      //  urlThumbnail = "http://"+urlThumbnail;

       // Toast.makeText(this, urlThumbnail, Toast.LENGTH_SHORT).show();

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    iv.setImageDrawable(null); // to clear the static background
                    iv.setBackground(new BitmapDrawable(response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    public void update_profile(final String userid, String f, String l, String e, String m, String filename, String URL) {


        String tag_json_obj = "json_obj_req";

        URL += userid + "&f_name=" + f + "&l_name=" + l + "&email=" + e + "&mob=" + m + "&profile_pic=" + filename;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String res = response.toString();
                       // finish();
                        Toast.makeText(EditUserProfileActivity.this, "Updated", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EditUserProfileActivity.this, "Unexpected network Error, please try again later", Toast.LENGTH_LONG).show();

            }
        });


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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



}
