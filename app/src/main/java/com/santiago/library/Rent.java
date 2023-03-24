package com.santiago.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Rent extends AppCompatActivity {

    EditText idUser, idBook, date;
    Button users, books, save, delete, edit, search, list ;

    bdLibrary bdLibrary = new bdLibrary(this, "librarydb", null, 2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        users = findViewById(R.id.btn6);
        books = findViewById(R.id.btn7);
        idUser = findViewById(R.id.et2);
        idBook = findViewById(R.id.et1);
        date = findViewById(R.id.et3);
        list = findViewById(R.id.btn5);
        save = findViewById(R.id.btn1);
        edit = findViewById(R.id.btn2);
        delete = findViewById(R.id.btn3);
        search = findViewById(R.id.btn4);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idUser.getText().toString().isEmpty() || idBook.getText().toString().isEmpty() || date.getText().toString().isEmpty()){
                    Toast.makeText(Rent.this, "you must fill all fields", Toast.LENGTH_SHORT).show();
                }
                else if(!idUser.getText().toString().isEmpty() && !idBook.getText().toString().isEmpty() && !date.getText().toString().isEmpty()){
                    SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                    String query0 = "select idUser from rent where idUser = '"+idUser.getText().toString()+"'and idBook = '"+idBook.getText().toString()+"'";
                    String query1 = "select idUser, status from user where idUser= '"+idUser.getText().toString()+"'";
                    String query2 = "select idBook, available from book where idBook = '"+idBook.getText().toString()+"'";
                    Cursor crUser = sdLibrary.rawQuery(query0,null);
                    Cursor cuUser = sdLibrary.rawQuery(query1, null);
                    Cursor cbBook = sdLibrary.rawQuery(query2, null);
                    if(crUser.moveToFirst()){
                        Toast.makeText(Rent.this, "User or book have a rent already", Toast.LENGTH_SHORT).show();
                    }else if(cuUser.moveToFirst() || cbBook.moveToFirst()){
                        if(cuUser.getInt(1) == 1 || cbBook.getInt(1) == 1){
                        Toast.makeText(Rent.this, "User penalized or book unavailable", Toast.LENGTH_SHORT).show();
                       }
                    }
                    else{
                        Toast.makeText(Rent.this, "User or book not registered", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    SQLiteDatabase sdLibraryW = bdLibrary.getWritableDatabase();
                    ContentValues cvRent = new ContentValues();
                    cvRent.put("idBook", idBook.getText().toString());
                    cvRent.put("idUser", idUser.getText().toString());
                    cvRent.put("date", date.getText().toString());
                    sdLibraryW.insert("rent", null, cvRent);
                    sdLibraryW.close();
                    Toast.makeText(Rent.this, "Rent successfully saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "select idUser from rent where idUser = '"+idUser.getText().toString()+"'";
                Cursor cRent = sdLibrary.rawQuery(query,null);
                if(cRent.moveToFirst()){//si encuentra la consulta
                    String deleteQuery = "delete from rent where idUser = '"+idUser.getText().toString()+"'";
                    sdLibrary.execSQL(deleteQuery);
                    Toast.makeText(Rent.this, "Rent was succesfully deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Rent.this, "Rent not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "select idUser, idBook, date from rent where idUser = '"+idUser.getText().toString()+"'";
                Cursor cRent = sdLibrary.rawQuery(query,null);
                if(cRent.moveToFirst()){// si encuentra la consulta
                    idBook.setText(cRent.getString(1));
                    date.setText(cRent.getString(2));
                }else{
                    Toast.makeText(Rent.this, "Rent not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "select idRent, idUser, idBook, date from rent where idUser = '"+idUser.getText().toString()+"'";
                Cursor cRent = sdLibrary.rawQuery(query,null);
                if(cRent.moveToFirst()){
                    String updateQuery = "update rent set idUser = '"+idUser.getText().toString()+"', idBook = '"+idBook.getText().toString()+"', date = '"+date.getText().toString()+"'";
                    sdLibrary.execSQL(updateQuery);
                    Toast.makeText(Rent.this, "Rent was successfully updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Rent.this, "unable to update rent", Toast.LENGTH_SHORT).show();
                }
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Book.class));
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),listRent.class));
            }
        });
    }
}