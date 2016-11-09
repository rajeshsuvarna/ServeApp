package com.infouna.serveapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.infouna.serveapp.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Darshan on 04-11-2016.
 */

public class EditUserProfileActivity extends AppCompatActivity {

    EditText fname, lname, mob, e_mail;
    ImageView iv;
    Button update;

    String profilepic;
    public static SharedPreferences spf;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit User Profile");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
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

        String f, l, e, p;

        spf = getSharedPreferences("MyPrefs.txt", Context.MODE_PRIVATE);
        f = spf.getString("edit_fname", "Null String");
        l = spf.getString("edit_lname", "Null String");
        e = spf.getString("edit_email", "Null String");
        p = spf.getString("edit_phone", "Null String");

        fname.setText(f);
        lname.setText(l);
        e_mail.setText(e);
        mob.setText(p);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = getIntent();

                String f = fname.getText().toString();
                String l = fname.getText().toString();
                String e = fname.getText().toString();
                String w = fname.getText().toString();

                SharedPreferences.Editor editor = spf.edit();
                editor.putString("edit_fname", f);
                editor.putString("edit_lname", l);
                editor.putString("edit_email", e);
                editor.putString("edit_phone", w);
                editor.commit();

                setResult(1);
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath); // Use this bitmap to convert to Base64
            BitmapDrawable bd = new BitmapDrawable(res, bitmap); // The received bitmap converted to drawable to attach it to imageview

            iv.setBackground(bd);

            String[] split = picturePath.split("/");

            profilepic = split[split.length - 1]; // the actual name of file Example: image.jpg
            //  Toast.makeText(getActivity(), ban_pic, Toast.LENGTH_SHORT).show();

        }

    }

    // call the below method to convert the bitmap to string
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
