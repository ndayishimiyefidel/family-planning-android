package com.example.familyplanning;

public class Users {
    private String fuln;
    private String idno;
    private String iphone;
    private String email;
    private String password;
    private  String sector;
    private  String cell;
    private String dob;
    private String gender;
    private String token;
    public Users() {
    }

    public Users(String fuln, String idno, String iphone, String email, String password, String sector, String cell, String dob, String gender, String token) {
        this.fuln = fuln;
        this.idno = idno;
        this.iphone = iphone;
        this.email = email;
        this.password = password;
        this.sector = sector;
        this.cell = cell;
        this.dob = dob;
        this.gender = gender;
        this.token = token;
    }

    public String getFuln() {
        return fuln;
    }
    public String getIdno() {
        return idno;
    }

    public String getIphone() {
        return iphone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public String getSector() {
        return sector;
    }


    public String getCell() {
        return cell;
    }


    public String getDob() {
        return dob;
    }


    public String getGender() {
        return gender;
    }


    public String getToken() {
        return token;
    }

}
