package com.example.familyplanning;

public class SelectMethod {
    private String userName;
    private String userEmail;
    private String userNid;
    private String userPhone;
    private String Ufpmethod;
    private String Ustartdate;
    private String Uenddate;
    private String duration;
    private String status;
    private String sector;
    private  String cell;
    private  String dob;
    private String token;
    private String gender;


    public SelectMethod() {
    }

    public SelectMethod(String userName, String userEmail, String userNid, String userPhone, String ufpmethod, String ustartdate, String uenddate, String duration, String status, String sector, String cell, String dob, String token, String gender) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNid = userNid;
        this.userPhone = userPhone;
        Ufpmethod = ufpmethod;
        Ustartdate = ustartdate;
        Uenddate = uenddate;
        this.duration = duration;
        this.status = status;
        this.sector = sector;
        this.cell = cell;
        this.dob = dob;
        this.token = token;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNid() {
        return userNid;
    }

    public void setUserNid(String userNid) {
        this.userNid = userNid;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUfpmethod() {
        return Ufpmethod;
    }

    public void setUfpmethod(String ufpmethod) {
        Ufpmethod = ufpmethod;
    }

    public String getUstartdate() {
        return Ustartdate;
    }

    public void setUstartdate(String ustartdate) {
        Ustartdate = ustartdate;
    }

    public String getUenddate() {
        return Uenddate;
    }

    public void setUenddate(String uenddate) {
        Uenddate = uenddate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
