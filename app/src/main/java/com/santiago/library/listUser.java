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

public class listUser extends AppCompatActivity {

    ListView listUser;
    ArrayList arrUsers;
    Button back;
    bdLibrary bdLibrary = new bdLibrary(this,"librarydb",null,2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        listUser = findViewById(R.id.listUser);
        back = findViewById(R.id.btn1);
        loadUsers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private void loadUsers() {
        arrUsers = retrieveUsers();
        // Generar el adaptador que pasará los datos al ListView
        ArrayAdapter<String> adpUsers = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,arrUsers);
        listUser.setAdapter(adpUsers);
    }

    private ArrayList<String> retrieveUsers() {
        ArrayList<String> userData = new ArrayList<String>();
        // Cargar los usuarios en el arraylist arrUsers;

        SQLiteDatabase sdLibrary = bdLibrary.getReadableDatabase();
        String qAllUsers = "Select idUser, name, email, password, status From user";
        Cursor cUsers = sdLibrary.rawQuery(qAllUsers,null);
        if (cUsers.moveToFirst()){
            do{
                // Generar un string para almacenar toda la información de cada usuario
                // y guardarlo en el arrayList
                String mStatus = cUsers.getInt(4) == 0 ? "active" : "penalized";
                String recUser = cUsers.getString(0)+" - "+cUsers.getString(1)+" - "+cUsers.getString(2)+" - "+mStatus;
                userData.add(recUser);
            }while(cUsers.moveToNext());
        }
        bdLibrary.close();
        return userData;
    }
}