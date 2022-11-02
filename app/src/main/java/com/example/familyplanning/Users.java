package com.example.familyplanning;

public class Users {
    private String fuln;
    private String idno;
    private String iphone;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String token;
    public Users() {
    }

    public Users(String fuln, String idno, String iphone, String email, String password, String dob, String gender, String token) {
        this.fuln = fuln;
        this.idno = idno;
        this.iphone = iphone;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.token = token;
    }

    public String getFuln() {
        return fuln;
    }

    public void setFuln(String fuln) {
        this.fuln = fuln;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
