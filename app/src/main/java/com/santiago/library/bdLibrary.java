package com.santiago.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class bdLibrary extends SQLiteOpenHelper {

    String tblBooks = "create table book(idBook int primary key, name Text, cost real, available integer)";
    String tblUsers = "create table user(idUser int primary key, name Text, email text, password int, status integer)";
    String tblRent = "create table rent(idRent int primary key autoincrement, idBook int, idStudent int, date text, foreign key (idBook) references book(idBook), foreign key (idUser) references student(idUser))";
    public bdLibrary(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblBooks);
        db.execSQL(tblUsers);
        db.execSQL(tblRent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists books");
        db.execSQL("drop table if exists student");
        db.execSQL("drop table if exists rent");
        db.execSQL(tblBooks);
        db.execSQL(tblUsers);
        db.execSQL(tblRent);

    }
}
