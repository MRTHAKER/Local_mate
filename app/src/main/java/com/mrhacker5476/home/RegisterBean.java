package com.mrhacker5476.home;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RegisterBean {
    String FirstName;
    String LastName;
    String Email;
    String Gender;
    String Password;
    String Mobile;
    String Img;


    public String encodeB(String s) throws UnsupportedEncodingException { return URLEncoder.encode(Img,"UTF-8"); }
    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "FirstName="+FirstName+"&LastName="+LastName+"&Email="+Email+"&Gender="+Gender+"&Password="+Password+"&Mobile="+Mobile;
    }
}
