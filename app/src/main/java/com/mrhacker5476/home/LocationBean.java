package com.mrhacker5476.home;

public class LocationBean
{
    String location;
    String email;

    @Override
    public String toString() {
        return "Location="+location+"&Email="+email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
