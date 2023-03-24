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

public class listRent extends AppCompatActivity {

    ListView listRent;
    ArrayList arrRent;
    Button back;
    bdLibrary bdLibrary = new bdLibrary(this,"librarydb",null,2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rent);

        listRent = findViewById(R.id.listRent);
        back = findViewById(R.id.btn1);
        loadUsers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Rent.class));
            }
        });
    }
    private void loadUsers() {
        arrRent = retrieveUsers();
        // Generar el adaptador que pasará los datos al ListView
        ArrayAdapter<String> adpUsers = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,arrRent);
        listRent.setAdapter(adpUsers);
    }

    private ArrayList<String> retrieveUsers() {
        ArrayList<String> rentData = new ArrayList<String>();
        // Cargar los usuarios en el arraylist arrUsers;

        SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
        String qAllUsers = "Select idRent, idUser, idBook, date From rent";
        Cursor cRent = sdLibrary.rawQuery(qAllUsers,null);
        if (cRent.moveToFirst()){
            do{
                // Generar un string para almacenar toda la información de cada renta
                // y guardarlo en el arrayList
                String recUser = cRent.getString(0)+" - "+cRent.getString(1)+" - "+cRent.getString(2)+" - "+cRent.getString(3);
                rentData.add(recUser);
            }while(cRent.moveToNext());
        }
        bdLibrary.close();
        return rentData;
    }
}