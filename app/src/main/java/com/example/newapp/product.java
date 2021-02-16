package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class product extends AppCompatActivity {
    ImageView imag;
    TextView codigo, nombre, categoria, cantidad, precio, descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db DB = new db(this, "registerp", null, 1);

        Bundle bundle = getIntent().getExtras();

        String code = bundle.getString("code");
        String name = bundle.getString("name");
        String desc = bundle.getString("desc");
        String pre = bundle.getString("pre");
        String cant = bundle.getString("cant");
        String cat = bundle.getString("cat");

        Bitmap image = DB.getImage(code);
        //imag.setImageBitmap(image);
    }
}