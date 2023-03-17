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
import android.widget.RadioButton;
import android.widget.Toast;

public class Book extends AppCompatActivity {

    EditText id, name, cost;
    RadioButton available, unAvailable;
    Button save, edit, search, delete, list, users, rent;

    bdLibrary bdLibrary = new bdLibrary(this, "librarydb" , null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        id = findViewById(R.id.et1);
        name = findViewById(R.id.et2);
        cost = findViewById(R.id.et3);
        available = findViewById(R.id.rb1);
        unAvailable = findViewById(R.id.rb2);
        save = findViewById(R.id.btn1);
        edit = findViewById(R.id.btn2);
        delete = findViewById(R.id.btn3);
        search = findViewById(R.id.btn4);
        list = findViewById(R.id.btn5);
        users = findViewById(R.id.btn6);
        rent = findViewById(R.id.btn7);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() && cost.getText().toString().isEmpty()){
                    Toast.makeText(Book.this, "you must fill all fields", Toast.LENGTH_SHORT).show();
                }else if(!name.getText().toString().isEmpty() && !cost.getText().toString().isEmpty()){
                    //este else verifica que no me deje guardar un nombre repetido
                    SQLiteDatabase sdlibrary = bdLibrary.getReadableDatabase();
                    String query = "select idBook, name from book where idBook = '"+id.getText().toString()+"'+&&+'"+name.getText().toString()+"'";
                    Cursor cBooks = sdlibrary.rawQuery(query, null);
                    if(cBooks.moveToFirst()){ //si encuentra un libro registrado
                        Toast.makeText(Book.this, "Book is already saved", Toast.LENGTH_SHORT).show();
                    }else {
                        SQLiteDatabase sdlibraryW = bdLibrary.getWritableDatabase();
                        ContentValues cvBooks = new ContentValues();
                        cvBooks.put("idBook", id.getText().toString());
                        cvBooks.put("name", name.getText().toString());
                        cvBooks.put("cost", cost.getText().toString());
                        cvBooks.put("available", available.isChecked() ? 0 : 1);
                        sdlibraryW.insert("book", null, cvBooks);
                        sdlibraryW.close();
                        Toast.makeText(Book.this, "book was successfully saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "select idBook from book where idBook = '"+id.getText().toString()+"'";
                Cursor cBooks = sdLibrary.rawQuery(query,null);
                if (cBooks.moveToFirst()){
                    String queryDelete = "delete from book where idBook = '"+id.getText().toString()+"'";
                    sdLibrary.execSQL(queryDelete);
                    Toast.makeText(Book.this, "book successfully deleted ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Book.this, "ID not found, try another one", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "select idBook, name, cost, available from book where idBook = '"+id.getText().toString()+"'";
                Cursor cBooks = sdLibrary.rawQuery(query,null);
                if(cBooks.moveToFirst()){
                    name.setText(cBooks.getString(1));
                    cost.setText(cBooks.getString(2));
                    if(cBooks.getInt(3) == 0){
                        available.setChecked(true);
                    }else{
                        unAvailable.setChecked(true);
                    }
                }else{
                    Toast.makeText(Book.this, "ID not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "Select idBook, name, cost, available From book Where idBook = '"+id.getText().toString()+"'";
                Cursor cUser = sdLibrary.rawQuery(query, null);
                if(cUser.moveToFirst()){
                    sdLibrary.execSQL("Update book set name = '"+name.getText().toString()+"', cost = '"+cost.getText().toString()+"" +
                            "', available = "+(available.isChecked() ? 0 : 1)+" where idBook = '"+id.getText().toString()+"'");
                    Toast.makeText(getApplicationContext(), "Book succesfully updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "you cannot edit a book if it's not registered",Toast.LENGTH_SHORT).show();
                }
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Rent.class));
            }
        });
    }
}