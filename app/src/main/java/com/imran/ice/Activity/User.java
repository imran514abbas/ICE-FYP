package com.imran.ice.Activity;

public class User {
    public String userid, first_name, phone_number, email, strdate, code;
    public double latitude, longitude;

    public User(String userid, String first_name, String phone_number, String email,
                String strdate, String code, double latitude, double longitude) {
        this.userid = userid;
        this.first_name = first_name;
        this.phone_number = phone_number;
        this.email = email;
        this.strdate = strdate;
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
