package com.example.newapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class home extends AppCompatActivity {

    Button selectimage, search, Logout, save, edit, delete;
    ImageView image;
    Bitmap imageDB;
    EditText txtCode, txtNombre, txtStock, txtDesc, txtPre, Codeprod;
    Spinner category;
    int selectpic=200;
    private CheckBox valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db DB = new db(this, "registerp", null, 1);

        Logout= findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }});

        Codeprod = (EditText)findViewById(R.id.codeprod);

        search = (Button) findViewById(R.id.btnbuscar);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String code = Codeprod.getText().toString();
                if (code.isEmpty()) Toast.makeText(home.this, "Ingresar el código del producto", Toast.LENGTH_SHORT).show();
                else {
                    Boolean find = DB.validcode(code);
                    if (find == false){
                        Toast.makeText(home.this, "El código no está registrado", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Cursor values = DB.getCursor(code);
                        String codigo = values.getString(0);
                        String nombre = values.getString(1);
                        String descripcion = values.getString(2);
                        String precio = values.getString(3);
                        String cantidad = values.getString(4);
                        String categoria = values.getString(5);

                        Intent intent  = new Intent(getApplicationContext(), product.class);

                        intent.putExtra("code", codigo);
                        intent.putExtra("name", nombre);
                        intent.putExtra("desc", descripcion);
                        intent.putExtra("pre", precio);
                        intent.putExtra("cant", cantidad);
                        intent.putExtra("cat", categoria);

                        startActivity(intent);
                    }
                }
            }});

        selectimage = findViewById(R.id.btnimage);
        image = findViewById(R.id.IVPreviewImage);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        //instancciar entradas de texto
        txtCode = findViewById(R.id.txtcode);
        txtNombre = findViewById(R.id.txtname);
        txtStock = findViewById(R.id.txtstock);
        txtDesc = findViewById(R.id.txtdesc);
        txtPre = findViewById(R.id.txtpre);

        //instsanciar listbox
        category = findViewById(R.id.spinner);

        //instanciar checkbox
        valid = (CheckBox) findViewById(R.id.checkBox);

        //instanciar botones
        save = (Button)findViewById(R.id.btnguardar);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //obtener texto de las entradas
                String codigo = txtCode.getText().toString();
                String nombre = txtNombre.getText().toString();
                String descripcion = txtDesc.getText().toString();
                String precio = txtPre.getText().toString();
                String cantidad = txtStock.getText().toString();

                //obtener elemento seleccionado de listbox
                String categoria = category.getSelectedItem().toString();

                if(image.getDrawable()==null){
                    Toast.makeText(home.this, "Debe seleccionar una imágen", Toast.LENGTH_LONG).show();
                }else{
                    //obtener bitmap de la imagen seleccionada

                    //image.invalidate();
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();

                    if(codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() ||cantidad.isEmpty()){
                        Toast.makeText(home.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
                    }else{

                        Boolean check = valid.isChecked();
                        if(check){
                            Boolean reg = DB.insertprod(codigo, nombre, descripcion, Float.parseFloat(precio), Integer.parseInt(cantidad), categoria, img);
                            if(reg==true)
                                Toast.makeText(home.this, "Producto registrado", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(home.this, "Ocurrio un error en el registro", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(home.this, "Debe marcar la casilla de verificación", Toast.LENGTH_SHORT).show();

                    }
                }
            }});

        edit= (Button)findViewById(R.id.btneditar);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //obtener texto de las entradas
                String codigo = txtCode.getText().toString();
                String nombre = txtNombre.getText().toString();
                String descripcion = txtDesc.getText().toString();
                String precio = txtPre.getText().toString();
                String cantidad = txtStock.getText().toString();

                //obtener elemento seleccionado de listbox
                String categoria = category.getSelectedItem().toString();

                if(image.getDrawable()==null){
                    Toast.makeText(home.this, "Debe seleccionar una imágen", Toast.LENGTH_LONG).show();
                }else{
                    //obtener bitmap de la imagen seleccionada
                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();

                    if(codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() ||cantidad.isEmpty()){
                        Toast.makeText(home.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
                    }else{
                        Boolean check = valid.isChecked();
                        if(check) {
                                Boolean checkupdatedata = DB.editprod(codigo, nombre, descripcion, Float.parseFloat(precio), Integer.parseInt(cantidad), categoria, img);
                                if (checkupdatedata == true)
                                    Toast.makeText(home.this, "Producto editado", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(home.this, "No existe o no se pudo editar este producto", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(home.this, "Debe marcar la casilla de verificación para editar el producto", Toast.LENGTH_SHORT).show();
                    }
                }
            }});

        delete= (Button)findViewById(R.id.btneliminar);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String codigo = txtCode.getText().toString();
               if (!codigo.isEmpty()){
                   Boolean check = valid.isChecked();
                   if(check) {
                       Boolean checkudeletedata = DB.deleteprod(codigo);
                       if (checkudeletedata == true)
                           Toast.makeText(home.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                       else
                           Toast.makeText(home.this, "No existe o no se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
                   }else Toast.makeText(home.this, "Debe marcar la casilla de verificación para eliminar un producto", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(home.this, "Debe ingresar el código del producto", Toast.LENGTH_SHORT).show();
               }
            }});
    }

    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), selectpic);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == selectpic) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }
}