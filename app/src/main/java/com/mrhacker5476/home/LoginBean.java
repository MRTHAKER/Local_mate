package com.mrhacker5476.home;

import androidx.annotation.NonNull;

public class LoginBean {
    String Email,Password;

    @NonNull
    @Override
    public String toString() {
        return "Email="+Email+"&Password="+Password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
