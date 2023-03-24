package com.santiago.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class listBooks extends AppCompatActivity {

    ListView listBook;
    ArrayList arrBook;
    Button back;
    bdLibrary bdLibrary = new bdLibrary(this,"librarydb",null,2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);

        listBook = findViewById(R.id.listBook);
        back = findViewById(R.id.btn1);
        loadUsers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Book.class));
            }
        });
    }
    private void loadUsers() {
        arrBook = retrieveUsers();
        // Generar el adaptador que pasará los datos al ListView
        ArrayAdapter<String> adpUsers = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,arrBook);
        listBook.setAdapter(adpUsers);
    }

    private ArrayList<String> retrieveUsers() {
        ArrayList<String> rentData = new ArrayList<String>();
        // Cargar los usuarios en el arraylist arrUsers;

        SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
        String qAllUsers = "Select idBook, name, cost, available From book";
        Cursor cBook = sdLibrary.rawQuery(qAllUsers,null);
        if (cBook.moveToFirst()){
            do{
                // Generar un string para almacenar toda la información de cada renta
                // y guardarlo en el arrayList
                String available = cBook.getInt(3) == 0 ? "available" : "unavailable";
                String recUser = cBook.getString(0)+" - "+cBook.getString(1)+" - "+cBook.getString(2)+" - "+available;
                rentData.add(recUser);
            }while(cBook.moveToNext());
        }
        bdLibrary.close();
        return rentData;
    }
}