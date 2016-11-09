package com.infouna.serveapp.activity;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.infouna.serveapp.R;

import java.io.ByteArrayOutputStream;


public class EditServiceProfileActivity extends AppCompatActivity {

    EditText add, web, loc, sname, ssname, sprice, tag, desc;
    String shop_pic, ban_pic;
    int i;
    ImageView iv_ban, iv_shop;
    Button update;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Service Profile");

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

        add = (EditText) findViewById(R.id.ES_address);
        web = (EditText) findViewById(R.id.ES_web);
        loc = (EditText) findViewById(R.id.ES_loc);
        sname = (EditText) findViewById(R.id.ES_sname);
        ssname = (EditText) findViewById(R.id.ES_subservice);
        sprice = (EditText) findViewById(R.id.ES_price);
        tag = (EditText) findViewById(R.id.ES_tag);
        desc = (EditText) findViewById(R.id.ES_desc);

        iv_ban = (ImageView) findViewById(R.id.ES_ban_pic);
        iv_shop = (ImageView) findViewById(R.id.ES_shop_pic);

        update = (Button) findViewById(R.id.ES_update);

        iv_ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });

        iv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath); // Use this bitmap to convert to Base64
            BitmapDrawable bd = new BitmapDrawable(res, bitmap); // The received bitmap converted to drawable to attach it to imageview

            String[] split = picturePath.split("/");

            if (i == 0) {
                iv_ban.setBackground(bd);
                ban_pic = split[split.length - 1]; // the actual name of file Example: image.jpg
                //  Toast.makeText(getActivity(), ban_pic, Toast.LENGTH_SHORT).show();

            } else if (i == 1) {
                iv_shop.setBackground(bd);
                shop_pic = split[split.length - 1]; // the actual name of file Example: image.jpg
                //  Toast.makeText(getActivity(), ban_pic, Toast.LENGTH_SHORT).show();

            }
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
