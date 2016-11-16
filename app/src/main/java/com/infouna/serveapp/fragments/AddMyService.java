package com.infouna.serveapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.infouna.serveapp.R;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static com.infouna.serveapp.activity.LoginActivity.MyPREFERENCES;

public class AddMyService extends Fragment {

    ArrayAdapter<String> adapterONE, adapterTWO;

    JsonObjectRequest jsonObjReq;
    List<String> list1, list2;

    String[] encodedParams = new String[10];

    public static final String MyPREFERENCES = "MyPrefs.txt";

    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    public static final String profile = "profilepicKey";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

/*
    @Bind(R.id.input_add_yourservicedescription) EditText jservdesc;
    @Bind(R.id.input_add_minserviceprice) EditText jminprice;
    @Bind(R.id.input_add_serviceaddress) EditText jsaddress;
    @Bind(R.id.input_add_servicecity) EditText jscity;
    @Bind(R.id.input_add_pincode) EditText jpin;
    @Bind(R.id.input_add_website) EditText jweb;
*/

    ImageView iv;
    EditText jservicetitle, jsubservice, jservdesc, jminprice, jsaddress, jscity, jpin, jweb;
    Button jregisterservice;

    public View v;

    private ProgressDialog pDialog;

    public static SharedPreferences spf;

    Spinner servicespinner, subservicespinner;

    String userid, ban_pic, shop_pic, loc, service, subservice, service_desc, min_price, address, stitle, city, pin, web;
    int enable = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_my_service, container, false);

        ButterKnife.bind(this.getActivity(), v);

        spf = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "Null String");

        prgDialog = new ProgressDialog(getActivity());
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Toast.makeText(getActivity(), userid + "test", Toast.LENGTH_SHORT).show();

        servicespinner = (Spinner) v.findViewById(R.id.servicespinner);
        subservicespinner = (Spinner) v.findViewById(R.id.subservicespinner);

        jservicetitle = (EditText) v.findViewById(R.id.input_add_servicetitle);
        // jsubservice = (EditText) v.findViewById(R.id.input_add_sub_service);
        jservdesc = (EditText) v.findViewById(R.id.input_add_yourservicedescription);
        jsaddress = (EditText) v.findViewById(R.id.input_add_serviceaddress);
        jminprice = (EditText) v.findViewById(R.id.input_add_minserviceprice);
        jpin = (EditText) v.findViewById(R.id.input_add_pincode);
        jscity = (EditText) v.findViewById(R.id.input_add_servicecity);
        jweb = (EditText) v.findViewById(R.id.input_add_website);
        jregisterservice = (Button) v.findViewById(R.id.btn_reg_add);

        iv = (ImageView) v.findViewById(R.id.imageholder);

        fill_spinner(AppConfig.URL_DASHBOARD_SERVICES_HOME, 1, "all"); // fill service spinner 1
        setAdapter(1);

        servicespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                adapterTWO = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, Collections.<String>emptyList());
                adapterTWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subservicespinner.setAdapter(adapterTWO);

                fill_spinner(AppConfig.URL_SUB_SEVICES_HOME, 2, servicespinner.getSelectedItem().toString());//servicespinner.getSelectedItem().toString());
                setAdapter(2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jregisterservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stitle = jservicetitle.getText().toString();
                service = servicespinner.getSelectedItem().toString(); //jservice.getText().toString();
                subservice = subservicespinner.getSelectedItem().toString(); //jsubservice.getText().toString();
                service_desc = jservdesc.getText().toString();
                min_price = jminprice.getText().toString();
                int foo_price = Integer.parseInt(min_price);
                address = jsaddress.getText().toString();
                city = jscity.getText().toString();
                pin = jpin.getText().toString();
                int foo_pin = Integer.parseInt(pin);
                web = jweb.getText().toString();

                Toast.makeText(getActivity(), "check" + service + " " + subservice, Toast.LENGTH_SHORT).show();

                /*

                if(service_desc.isEmpty())
                {
                    jservdesc.setError("Service Description Required!!");
                }
                    else if(service_desc.length() < 5)
                    {
                        jservdesc.setError("Service Description too short!!");
                    }
                        else if (min_price.isEmpty())
                        {
                            jminprice.setError("Please Provide Your Minimum Service Price!!");
                        }
                            else if (foo_price <= 99)
                            {
                                jminprice.setError("Minimum Service Price Should Be More Than 100 RS");
                            }
                                else if (address.isEmpty())
                                {
                                    jsaddress.setError("Please provide your address");
                                }
                                    else  if(address.length() < 7)
                                    {
                                      jsaddress.setError("Address very short, please enter proper address!!");
                                    }
                                        else  if(city.isEmpty())
                                        {
                                            jscity.setError("Please enter your servicing city");
                                        }
                                        else if(pin.isEmpty())
                                            {
                                                jpin.setError("Please provide your PIN-CODE");
                                            }
                                            else if(foo_pin < 2)
                                            {
                                                jpin.setError("Please provide correct PIN-CODE");

                                            }
                                                else if(Patterns.WEB_URL.matcher(web).matches())
                                                {
                                                    jweb.setError("Please provide your proper website");
                                                }
                                                else
                */

                try {
                    uploadImage(v);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enable == 1) {

                    loadImagefromGallery();
                }
            }
        });

        return v;
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        iv.setImageResource(0);
    }

    // When Image is selected from Gallery
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
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
                Toast.makeText(
                        getActivity(),
                        fileName,
                        Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage(View v) throws UnsupportedEncodingException, URISyntaxException {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Initiating...");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();

            register(userid, service, subservice, fileName, "null", "location", service_desc, min_price, address, city, pin, web, stitle, AppConfig.URL_ADD_SERVICE);

            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getActivity(),
                    "Please select an image",
                    Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), String.format("Image updated successfully"), Toast.LENGTH_LONG).show();
                //showConfirmDialog();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            "Server not responding",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getActivity(), "Device not connected to Internet. Try again", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void fill_spinner(String url, final int spin, String servicename) {

        if (spin == 2) {
            url += "&s_name=" + servicename;
        }
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();

        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (spin == 1) {
                        JSONArray dash = response.getJSONArray("dashboard_services");

                        JSONObject jsonObject;

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);
                            String a = jsonObject.getString("services");

                            list1.add(a);
                        }

                    } else if (spin == 2) {
                        JSONArray dash = response.getJSONArray("sub_service_list");

                        if (response.getString("result").equals("1")) {

                            JSONObject jsonObject;

                            for (int i = 0; i < dash.length(); i++) {

                                jsonObject = dash.getJSONObject(i);

                                String a = jsonObject.getString("sub_service_name");

                                list2.add(a);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No subservices", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void setAdapter(final int spinner) {
        new Handler().postDelayed(new Runnable() {  //adding a delay to ensure the list has been populated
            @Override
            public void run() {
                if (spinner == 1) {
                    adapterONE = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list1);
                    adapterONE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    servicespinner.setAdapter(adapterONE);

                    fill_spinner(AppConfig.URL_SUB_SEVICES_HOME, 2, (String) servicespinner.getSelectedItem());//servicespinner.getSelectedItem().toString());
                    setAdapter(2);

                } else if (spinner == 2) {

                    adapterTWO = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list2);
                    adapterTWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subservicespinner.setAdapter(adapterTWO);
                }
            }
        }, 2000);

        enable = 1;

    }

    private void register(String userid, String service, String subservice, String ban_pic, String shop_pic, String loc, String service_desc, String min_price, String address, String city, String pin, String web, String stitle, String URL) throws UnsupportedEncodingException, URISyntaxException {

//        pDialog.setMessage("Registering Service Provider......");
        //      showDialog();

        encodedParams[0] = URLEncoder.encode(address, "utf-8");
        encodedParams[1] = URLEncoder.encode(web, "utf-8");
        encodedParams[2] = URLEncoder.encode(min_price, "utf-8");
        encodedParams[3] = URLEncoder.encode(stitle, "utf-8");
        encodedParams[4] = URLEncoder.encode(service_desc, "utf-8");
        encodedParams[5] = URLEncoder.encode(pin, "utf-8");
        encodedParams[6] = URLEncoder.encode(subservice, "utf-8");

        URL += "&userid=" + userid + "&add=" + encodedParams[0] + "&ban_pic=" + ban_pic + "&website=" + encodedParams[1] +
                "&shop_pic=" + shop_pic + "&loc=" + loc + "&s_name=" + service + "&s_price=" + encodedParams[2] +
                "&s_sub_name=" + encodedParams[6] + "&s_title=" + encodedParams[3] + "&s_desc=" + encodedParams[4] +
                "&pin=" + encodedParams[5];

        Toast.makeText(getActivity(), URL, Toast.LENGTH_SHORT).show();

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String res = response.getString("spid");
                            SharedPreferences.Editor editor = spf.edit();
                            editor.putString("spidKey", res);
                            editor.commit();

                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}