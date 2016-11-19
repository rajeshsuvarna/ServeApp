package com.infouna.serveapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.infouna.serveapp.R;
import com.infouna.serveapp.activity.HomeActivity;
import com.infouna.serveapp.activity.NotificationActivity;

/**
 * Created by Darshan on 18-11-2016.
 */

public class NotificationBaseFragment extends Fragment {

    LinearLayout linearuser, linearsp, linearoffers;

    String check, userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_base, container, false);

        linearuser = (LinearLayout) v.findViewById(R.id.llun);
        linearsp = (LinearLayout) v.findViewById(R.id.llspn);
        linearoffers = (LinearLayout) v.findViewById(R.id.lloffers);

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
                    final MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getActivity());
                    builder.setTitle("You are not Service Provider");
                    builder.setDescription("You are not an service provider, please add your service to get orders and notified... ");
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

            }
        });

        linearoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "No offers now, we will notify you when there are offers", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
