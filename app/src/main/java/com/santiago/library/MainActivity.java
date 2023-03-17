package com.santiago.library;

import androidx.activity.result.contract.ActivityResultContracts;
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

public class MainActivity extends AppCompatActivity {

EditText id, name, email, pass;
RadioButton activo, sancion;
Button save, edit, delete, search, list, books, rent;

bdLibrary bdLibrary = new bdLibrary(this, "librarydb" , null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.et0);
        name = findViewById(R.id.et1);
        email = findViewById(R.id.et2);
        pass = findViewById(R.id.et3);
        activo = findViewById(R.id.rb1);
        sancion = findViewById(R.id.rb2);
        save = findViewById(R.id.btnsave);
        edit = findViewById(R.id.btnedit);
        delete = findViewById(R.id.btndelete);
        search = findViewById(R.id.btnsearch);
        list = findViewById(R.id.btnlist);
        books = findViewById(R.id.btnbook);
        rent = findViewById(R.id.btnrent);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){

                    SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                    String query = "Select email From User Where email = '"+email.getText().toString()+"'";
                    Cursor cUser = sdLibrary.rawQuery(query, null);
                    if (!cUser.moveToFirst()){ // No encuentra el email ingresado

                        SQLiteDatabase sdLibraryW = bdLibrary.getWritableDatabase();

                        ContentValues contentUser = new ContentValues();
                        contentUser.put("idUser", id.getText().toString());
                        contentUser.put("name", name.getText().toString());
                        contentUser.put("email", email.getText().toString());
                        contentUser.put("password", pass.getText().toString());
                        contentUser.put("status", activo.isChecked() ? 0 : 1);
                        sdLibraryW.insert("User",null,contentUser);
                        sdLibraryW.close();
                        Toast.makeText(getApplicationContext(),"user saved succesfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"User alredy exists, try another one",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"please fill every field",Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "Select idUser From user Where idUser = '"+id.getText().toString()+"'";
                Cursor cUser = sdLibrary.rawQuery(query, null);
                if(cUser.moveToFirst()){
                    String deleteQuery = "delete from user where idUser = '"+id.getText().toString()+"'";
                    sdLibrary.execSQL(deleteQuery);
                    Toast.makeText(getApplicationContext(), "User succesfullly deleted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "ID not found, try another one",Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "Select idUser, name, email, status From user Where idUser = '"+id.getText().toString()+"'";
                Cursor cUser = sdLibrary.rawQuery(query, null);
                if(cUser.moveToFirst()){
                    name.setText(cUser.getString(1));
                    email.setText(cUser.getString(2));
                    if(cUser.getInt(3) == 0){
                        activo.setChecked(true);
                    }else{
                        sancion.setChecked(true);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "ID not found, try another one",Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
                String query = "Select idUser, name, email, password, status From user Where idUser = '"+id.getText().toString()+"'";
                Cursor cUser = sdLibrary.rawQuery(query, null);
                if(cUser.moveToFirst()){
                    sdLibrary.execSQL("Update User set name = '"+name.getText().toString()+"', email= '"+email.getText().toString()+"" +
                            "', status = "+(activo.isChecked() ? 0 : 1)+" where idUser = '"+id.getText().toString()+"'");
                    Toast.makeText(getApplicationContext(), "User succesfully updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "you cannot edit an user if it's not signed up",Toast.LENGTH_SHORT).show();
                }
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Book.class));
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