package com.infouna.serveapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.HomeActivity;
import com.infouna.serveapp.activity.OrderDetailsSPActivity;
import com.infouna.serveapp.adapters.RVAdapter;
import com.infouna.serveapp.adapters.ServiceOrdersAdapter;
import com.infouna.serveapp.app.AppConfig;
import com.infouna.serveapp.app.AppController;
import com.infouna.serveapp.datamodel.OrderListCardSP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceOrders extends Fragment {

    ServiceOrdersAdapter adapter;
    RecyclerView recyclerView;

    JsonObjectRequest jsonObjReq;

    private ProgressDialog pDialog;

    public List<OrderListCardSP> data;

    public static final int HOME = 0;
    public static final int ORDERLISTSP = 1;
    public static final int ORDERLISTUSER = 2;
    public static final int SERVICELIST = 3;
    public static final int NOTIFICATION = 4;

    private int mDatasetTypes[] = {HOME, ORDERLISTSP, ORDERLISTUSER, SERVICELIST, NOTIFICATION};

    TextView total_orders;

    String userid = "", spid = "", type = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_orders, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        pDialog.setIndeterminate(true);
        pDialog.setMessage("Loading...");
        showDialog();

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        userid = spf.getString("useridKey", "null");
        type = spf.getString("typeKey", "null");
        spid = spf.getString("spidKey", "null");

   //     Toast.makeText(getActivity(), type, Toast.LENGTH_SHORT).show();

        total_orders = (TextView) v.findViewById(R.id.t_orders);

        if (type.equals("USER")) {
            //Toast.makeText(getActivity(), "Oops! You are not a Service Provider", Toast.LENGTH_SHORT).show();
            hideDialog();
            final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getActivity());
            builder.setTitle("You are not Service Provider");
            builder.setDescription("Please add your service to get orders and earn dollars... ");
            builder.withDialogAnimation(true, Duration.SLOW);
            builder.setStyle(Style.HEADER_WITH_TITLE);
            builder.setPositiveText("OK");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                    builder.autoDismiss(true);
                }
            });
            builder.show();
        } else if (type.equals("SP")) {


            data = fill_with_data(AppConfig.ORDER_LISTING_SP, spid);
            // pass spid as 2nd parameter here

            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewODSP);

            adapter = new ServiceOrdersAdapter(data, mDatasetTypes[0]); //array position is [0] coz card card type is HOME

            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

            itemAnimator.setAddDuration(1000);
            itemAnimator.setRemoveDuration(1000);
            recyclerView.setItemAnimator(itemAnimator);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.addOnItemTouchListener(new RVAdapter.RecyclerTouchListener(getActivity(), recyclerView, new RVAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent i = new Intent(getActivity(), OrderDetailsSPActivity.class);
                    i.putExtra("reqid", data.get(position).reqid);
                    i.putExtra("spid", spid);
                    startActivity(i);
                }

                @Override
                public void onLongClick(View view, int position) {

                    Intent i = new Intent(getActivity(), OrderDetailsSPActivity.class);
                    i.putExtra("reqid", data.get(position).reqid);
                    i.putExtra("spid", spid);
                    startActivity(i);
                }
            }));

        }

            return v;

    }

    public List<OrderListCardSP> fill_with_data(String url, String spid) {

        data = new ArrayList<>();

        url += "&spid=" + spid;


        jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String res = response.getString("result");

                    if (res.equals("1")) {

                        total_orders.setText(response.getString("total_orders"));

                        JSONArray dash = response.getJSONArray("sp_orders");

                        JSONObject jsonObject;

                        data.clear();

                        for (int i = 0; i < dash.length(); i++) {

                            jsonObject = dash.getJSONObject(i);

                            String a = jsonObject.getString("reqid"), b = jsonObject.getString("spid"),
                                    c = jsonObject.getString("service_name"), d = jsonObject.getString("location"),
                                    e = jsonObject.getString("username"), f = jsonObject.getString("requested_date_time"),
                                    g = jsonObject.getString("accepted");

                            data.add(new OrderListCardSP(a, b, c, d, e, f, g));
                        }
                        hideDialog();
                    }
                    else {

                        hideDialog();

                        final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getActivity());
                        builder.setTitle("You haven't received any service yet");
                        builder.setDescription("Keep calm and wait for orders... ");
                        builder.withDialogAnimation(true, Duration.SLOW);
                        builder.setStyle(Style.HEADER_WITH_TITLE);
                        builder.setPositiveText("OK");
                        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent i = new Intent(getActivity(), HomeActivity.class);
                                startActivity(i);
                                builder.autoDismiss(true);
                            }
                        });
                        builder.show();



                    }
                    adapter.notifyDataSetChanged();
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

        return data;
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