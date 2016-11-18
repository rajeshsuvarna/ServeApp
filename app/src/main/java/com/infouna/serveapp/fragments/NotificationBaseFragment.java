package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.NotificationActivity;

/**
 * Created by Darshan on 18-11-2016.
 */

public class NotificationBaseFragment extends Fragment {

    LinearLayout linearuser, linearsp;

    String check, userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_base, container, false);

        linearuser = (LinearLayout) v.findViewById(R.id.llun);
        linearsp = (LinearLayout) v.findViewById(R.id.llspn);

        SharedPreferences spf = this.getActivity().getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        String userid = spf.getString("useridKey", "");
        check = spf.getString("typeKey", "");

        linearuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NotificationActivity.class);
                i.putExtra("ID", "USER");
                startActivity(i);
            }
        });

        linearsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.equals("SP")) {
                    Intent i = new Intent(getActivity(), NotificationActivity.class);
                    i.putExtra("ID", "SP");
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "You are not a Service Provider", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }
}
