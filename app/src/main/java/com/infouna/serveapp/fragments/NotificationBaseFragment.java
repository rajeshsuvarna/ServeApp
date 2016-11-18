package com.infouna.serveapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.NotificationActivity;

/**
 * Created by Darshan on 18-11-2016.
 */

public class NotificationBaseFragment extends Fragment {

    LinearLayout linearuser, linearsp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_base, container, false);

        linearuser = (LinearLayout) v.findViewById(R.id.llun);
        linearsp = (LinearLayout) v.findViewById(R.id.llspn);

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
                Intent i = new Intent(getActivity(), NotificationActivity.class);
                i.putExtra("ID", "SP");
                startActivity(i);
            }
        });

        return v;
    }
}
