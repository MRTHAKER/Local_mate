package com.mrhacker5476.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;

public class Helper extends SQLiteOpenHelper {
    public static int version =1;
    public String table;
    public static String name="Local_mate";
    public Helper(@Nullable Context context,String table) {
        super(context, name, null, version);
        this.table=table;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+table+"(" +
                "Id" +
                " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," +
                "FirstName" +
                " TEXT NOT NULL" +
                "," +
                "LastName" +
                " TEXT NOT NULL" +
                "," +
                "Email" +
                " TEXT NOT NULL" +
                "," +
                "Gender" +
                " TEXT NOT NULL" +
                "," +
                "Mobile" +
                " TEXT NOT NULL" +
                "," +
                "Password " +
                "PASSWORD NOT NULL" +
                ")"
        );
    }
    public void Insert(ContentValues cv){
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(table,null,cv);
    }
    public Cursor Where(String CompareField,String[] columns,String[] Columns_ToCompare){
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor c=db.query(table,columns,CompareField,Columns_ToCompare,null,null,null);
        return c;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
