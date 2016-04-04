package com.infouna.serveapp.fragments;

 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import android.support.annotation.Nullable;
 import android.support.v4.app.Fragment;
 import android.support.v4.content.ContextCompat;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.Toast;

 import com.infouna.serveapp.R;
 import com.infouna.serveapp.activity.RateServiceActivity;

/**
 * Created by infouna
 */
public class UserProfile extends Fragment {

    Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
         ctx = v.getContext();

        Button bt = (Button) v.findViewById(R.id.button_edit_my_profile);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, RateServiceActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
}