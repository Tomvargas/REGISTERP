package com.example.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class db extends SQLiteOpenHelper {
    private static final String SqlCreateusr = "CREATE TABLE IF NOT EXISTS usuarios(user TEXT PRIMARY KEY, pass TEXT);";//consulta para crear tabla usuarios
    //consulta para crear tabla productos
    private static final String SqlCreatepro = "CREATE TABLE IF NOT EXISTS productos(code TEXT PRIMARY KEY, name TEXT, des TEXT, pre REAL, cant INTEGER, categoria TEXT, img BLOB NOT NULL);";

    public db(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //ejecuta las consultas declaradas al inicio de la clase
        db.execSQL(SqlCreateusr);
        db.execSQL(SqlCreatepro);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists usuarios");
        db.execSQL("drop Table if exists productos");
    }

    //inserta un usuario a la base de datos
    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        //contenedor de valores para insertar een la base de datos
        ContentValues contentValues= new ContentValues();
        contentValues.put("user", username);
        contentValues.put("pass", password);

        long result = MyDB.insert("usuarios", null, contentValues);//metodo para ingresar las variables en la base de datos
        //retorna verdadero o falso segun el resultado
        if(result==-1) return false;
        else
            return true;
    }

    //Verifica si el usuario ingresado ya esta registrado en la base de datos
    public Boolean checkuser(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        //Busca el usuario ingresado en la base de datos
        Cursor cursor = MyDB.rawQuery("Select * from usuarios where user = ?", new String[]{username});
        //retorna verdadero o falso si lo encuentra o no
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //Valida que el usuario y contraseña ingresada sean las registradas en la base de datos
    public Boolean loginvalid(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from usuarios where user = ? and pass = ?", new String[] {username,password});
        //retorna verdadero o falso si las credenciales están en la BD o no
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    //Ingresa un producto a la base de datos
    public boolean insertprod(String code, String name, String des, Float pre, int cant, String categoria,Bitmap img ){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("des", des);
        contentValues.put("pre", pre);
        contentValues.put("cant", cant);
        contentValues.put("categoria", categoria);
        contentValues.put("img", data);
        long ins = MyDB.insert("productos", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    public Boolean editprod(String code, String name, String des, Float pre, int cant, String categoria, byte[] img ) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("des", des);
        contentValues.put("pre", pre);
        contentValues.put("cant", cant);
        contentValues.put("categoria", categoria);
        contentValues.put("img", img);
        Cursor cursor = DB.rawQuery("Select * from productos where code = ?", new String[]{code});
        if (cursor.getCount() > 0) {
            long result = DB.update("productos", contentValues, "code=?", new String[]{code});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }}

    public Boolean deleteprod (String code)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from productos where code = ?", new String[]{code});
        if (cursor.getCount() > 0) {
            long result = DB.delete("productos", "code=?", new String[]{code});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public Boolean validcode (String code)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from productos where code = ?", new String[]{code});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public Cursor getCursor(String code){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from productos where code = ?", new String[]{code});
        cursor.moveToFirst();
        return cursor;
    }


    public Bitmap getImage(String code){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select img from productos where code = ?", new String[]{code});
        if(cursor.moveToFirst()) {
            byte[] bitmap = cursor.getBlob(0);
            cursor.close();
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0 , bitmap.length);
            return image;
        }
        if(cursor !=null && !cursor.isClosed()){
            cursor.close();
        }
        return null;
    }
}
