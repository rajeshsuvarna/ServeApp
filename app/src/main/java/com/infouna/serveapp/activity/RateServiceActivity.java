package com.infouna.serveapp.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.infouna.serveapp.R;

import butterknife.Bind;

/**
 * Created by Darshan on 31-03-2016.
 */
public class RateServiceActivity extends Activity {

    ImageButton stars[] = new ImageButton[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_service);

        stars[0] = (ImageButton) findViewById(R.id.star_1);
        stars[1] = (ImageButton) findViewById(R.id.star_2);
        stars[2] = (ImageButton) findViewById(R.id.star_3);
        stars[3] = (ImageButton) findViewById(R.id.star_4);
        stars[4] = (ImageButton) findViewById(R.id.star_5);

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.star_1:
                rater(1);
                break;
            case R.id.star_2:
                rater(2);
                break;
            case R.id.star_3:
                rater(3);
                break;
            case R.id.star_4:
                rater(4);
                break;
            case R.id.star_5:
                rater(5);
                break;
        }

    }

    public void rater(int rating)
    {
        clear_rating();
        for(int i = 0;i < rating; i++)
            stars[i].setImageResource(R.mipmap.ic_star_selected);
    }

    public void clear_rating()
    {
        for(int i = 0;i < 5; i++)
            stars[i].setImageResource(R.mipmap.ic_star_deselected);
    }

}