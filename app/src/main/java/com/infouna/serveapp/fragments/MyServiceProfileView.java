package com.infouna.serveapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.infouna.serveapp.activity.HomeActivity;
import com.infouna.serveapp.activity.LoginActivity;
import com.infouna.serveapp.activity.UserRegistrationActivity;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.ServiceProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyServiceProfileView extends Fragment {
    TextView jyourservice, jsubservice, jservicedesc, jminprice, jaddress, jweb;
    Button jedit;

    String userid = "", spid = "", type = "";

    private ProgressDialog pDialog;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_profile_view, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "");
        type = spf.getString("typeKey", "");

        if (type.equals("SP")) {
            Toast.makeText(getActivity(), "Loading data", Toast.LENGTH_SHORT).show();

            fetch_profile(userid);
        } else if (type.equals("USER")) {
            Toast.makeText(getActivity(), "Oops! You are not a Service Provider", Toast.LENGTH_SHORT).show();
            final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getActivity());
            builder.setTitle("You are not Service Provider");
            builder .setDescription("Please add your service to continue... ");
            builder.withDialogAnimation(true, Duration.SLOW);
            builder.setStyle(Style.HEADER_WITH_TITLE);
            builder.setPositiveText("OK");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent i =new Intent(getActivity(),HomeActivity.class);
                    startActivity(i);
                    builder.autoDismiss(true);
                }
            });
            builder.show();
        }

        jyourservice = (TextView) v.findViewById(R.id.textview_your_service);
        jsubservice = (TextView) v.findViewById(R.id.textview_sub_service);
        jservicedesc = (TextView) v.findViewById(R.id.textview_service_desc);
        jminprice = (TextView) v.findViewById(R.id.textview_min_service_price);
        jaddress = (TextView) v.findViewById(R.id.textview_address);
        jweb = (TextView) v.findViewById(R.id.textview_your_website);

        jedit = (Button) v.findViewById(R.id.button_edit_my_service_profile);

        jedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    public void fetch_profile(String userid) {

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Loading...");
        showDialog();
        String tag_json_obj = "json_obj_req";

        String url = AppConfig.URL_GET_SERVICE_PROFILE;

        url += userid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String res = response.getString("result");
                         //   Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();



                            if (res.equals("0")) {
                                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                                hideDialog();
                            } else {
                                JSONArray dash = response.getJSONArray("sp_profile");
                              //  Toast.makeText(getActivity(), dash.toString(), Toast.LENGTH_SHORT).show();

                                JSONObject jsonObject = dash.getJSONObject(0);

                                jyourservice.setText(jsonObject.getString("service_name"));
                                jsubservice.setText(jsonObject.getString("sub_service_name"));
                                jminprice.setText(jsonObject.getString("service_price"));
                                jaddress.setText(jsonObject.getString("address"));
                                jweb.setText(jsonObject.getString("website"));


                                String a = jsonObject.getString("first_name"), b = jsonObject.getString("last_name"),
                                        c = jsonObject.getString("email"), d = jsonObject.getString("profile_pic"),
                                        e = jsonObject.getString("address"), f = jsonObject.getString("logo_path"),
                                        g = jsonObject.getString("shop_photos"), h = jsonObject.getString("website"),
                                        i = jsonObject.getString("location"), j = jsonObject.getString("service_id"),
                                        k = jsonObject.getString("service_name"), l = jsonObject.getString("sub_service_name"),
                                        m = jsonObject.getString("tags"), n = jsonObject.getString("service_price");

                                ServiceProfile sp = new ServiceProfile(
                                        a, b,
                                        c, d,
                                        e, f,
                                        g, h,
                                        i, j,
                                        k, l,
                                        m, n);



                               jyourservice.setText(sp.service_name);
                                jsubservice.setText(sp.sub_service_name);
                                jservicedesc.setText("");     // nothing in URL
                                jminprice.setText(sp.service_price);
                                jaddress.setText(sp.address);
                                jweb.setText(sp.website);

                                loadImages(d);
                                hideDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }
                }

                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }

        );

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

    private void loadImages(String urlThumbnail) {

        if (!urlThumbnail.equals("NA")) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                    // bgimage.setBackground(new BitmapDrawable(response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

}