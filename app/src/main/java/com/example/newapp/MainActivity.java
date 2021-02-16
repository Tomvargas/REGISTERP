package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newapp.db;

public class MainActivity extends AppCompatActivity {
    private EditText txtNombre, txtPass;//variables para las entradas de texto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //entradas de texto
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtPass = (EditText)findViewById(R.id.txtPass);

        //instanciar la base de datos
        db DB = new db(this, "registerp", null, 1);

        //Boton iniciar sesion
        final Button Entrar= findViewById(R.id.btnEntrar);
        Entrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Obtener valores de la entrada de texto
                String user = txtNombre.getText().toString();
                String pass = txtPass.getText().toString();

                //validar que los campos no esten vacios
                if(user.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                else{
                    //validar en la BD el inicio de sesion
                    Boolean checkuserpass = DB.loginvalid(user, pass);
                    if(checkuserpass==true){
                        //Ingreso exitoso, direcciona a la página home
                        Toast.makeText(MainActivity.this, "Hola, "+user, Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), home.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //boton registrar usuario
        final Button Registrar= findViewById(R.id.btnRegistrar);
        Registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //obtener valores de las entradas de texto
                String user = txtNombre.getText().toString();
                String pass = txtPass.getText().toString();

                //validar que no esten vacias las entradas de texto
                if(user.equals("")||pass.equals("")){
                    Toast.makeText(MainActivity.this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    //Validar que en el campo user no se introduzcan numeros
                    if(sololetra(user)){
                        //Verificar nombre de usuario en la base de datos
                        Boolean checkuser = DB.checkuser(user);
                        if(checkuser==false){
                            //Registrar usuario en la base de datos
                            Boolean insert = DB.insertData(user, pass);
                            if(insert==true){
                                Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Registro no completado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Este usuario ya está registrado, intente con otro nombre de usuario", Toast.LENGTH_SHORT).show();
                        }

                    }else
                        Toast.makeText(MainActivity.this, "El campo usuario no puede contener numeros", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //metodo para validar solo letras en una cadena
    public boolean sololetra(String str) {
        str = str.toLowerCase();

        char[] charArray = str.toCharArray();//transforma la sstring en una array de caracteres
        //recorre la array de caracteres
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            //valida cada caracter entre a y z, si encuentra un numero retorna inmediatamente falso.
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }
        return true;
    }
}