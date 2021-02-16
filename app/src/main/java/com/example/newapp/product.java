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
import android.widget.Toast;

public class product extends AppCompatActivity {
    ImageView imag;
    TextView codigo, nombre, categoria, cantidad, precio, descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        imag = findViewById(R.id.IMG);
        codigo = findViewById(R.id.CODIGO);
        nombre = findViewById(R.id.NOMBRE);
        categoria = findViewById(R.id.CATEGORIA);
        cantidad = findViewById(R.id.CANTIDAD);
        precio = findViewById(R.id.PRECIO);
        descripcion = findViewById(R.id.DESCRIPCION);

        db DB = new db(this, "registerp", null, 1);

        Bundle bundle = getIntent().getExtras();

        String code = "ID: "+bundle.getString("code");
        String name = bundle.getString("name");
        String desc = bundle.getString("desc");
        String pre = "$"+bundle.getString("pre");
        String cant = "Stock: "+bundle.getString("cant");
        String cat = "Categoría: "+bundle.getString("cat");

        Bitmap image = DB.getImage(bundle.getString("code") );

        if(image==null){
            Toast.makeText(product.this, "No hay imagen del producto", Toast.LENGTH_SHORT).show();
        }else{
            imag.setImageBitmap(image);
        }

        nombre.setText(name);
        codigo.setText(code);
        descripcion.setText(desc);
        precio.setText(pre);
        cantidad.setText(cant);
        categoria.setText(cat);
    }
}