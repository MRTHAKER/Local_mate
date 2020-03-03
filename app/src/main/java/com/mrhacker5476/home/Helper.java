package com.mrhacker5476.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Helper extends SQLiteOpenHelper {
    public static int version =1;
    public static String table="ok";
    public static String name="home";
    RegisterBean rb;
    public Helper(@Nullable Context context,RegisterBean rb) {
        super(context, name, null, version);
        this.rb=rb;

    }
    public Helper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+table+"(" +
                "Id" +
                "INTEGER PRIMARY KEY AUTOINCREMENT" +
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
    public void Insert(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FirstName",rb.getFirstName());
        cv.put("LastName",rb.getLastName());
        cv.put("Email",rb.getEmail());
        cv.put("Gender",rb.getGender());
        cv.put("Mobile",rb.getMobile());
        cv.put("Password",rb.getPassword());
        db.insert(table,null,cv);
    }
    /*public Boolean CheckLog(String name,String pass){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select FirstName,Password from "+table+" where FirstName='"+name+"'",null);
        c.moveToNext();
        if(c.getString(0)!=null) return Boolean.TRUE;
        else return Boolean.FALSE;
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
