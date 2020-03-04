package com.mrhacker5476.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Register_loginSource {
    RegisterBean rb;
    Context context;
    Helper h;
    public static String Table_Name="register";
    public static String Field_FirstName="FirstName";
    public static String Field_LastName="LastName";
    public static String Field_Email="Email";
    public static String Field_Gender="Gender";
    public static String Field_Mobile="Mobile";
    public static String Field_Password="Password";

    public Register_loginSource(RegisterBean rb,Context context) {
        this.rb = rb;
        this.context=context;
        h = new Helper(context,Table_Name);
    }

    public void insert(){
        ContentValues cv = new ContentValues();
        cv.put(Field_FirstName,rb.getFirstName());
        cv.put(Field_LastName,rb.getLastName());
        cv.put(Field_Email,rb.getEmail());
        cv.put(Field_Gender,rb.getGender());
        cv.put(Field_Mobile,rb.getMobile());
        cv.put(Field_Password,rb.getPassword());
        h.Insert(cv);
    }
    public Boolean CheckLogin(RegisterBean ck){
        String[] Columns={Field_Email,Field_Password};
        String compareField=Field_Email+"=?";
        String[] column_Tocompare={ck.getEmail()};
        Cursor c= h.Where(compareField,Columns,column_Tocompare);
       if(c.moveToFirst()){
          String pass=c.getString(c.getColumnIndex(Field_Password));
          if(pass.equals(ck.getPassword()))return Boolean.TRUE;
          else return Boolean.FALSE;
       }
       else return Boolean.FALSE;
    }
}
