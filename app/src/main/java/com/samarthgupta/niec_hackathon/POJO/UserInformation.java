package com.samarthgupta.niec_hackathon.POJO;

/**
 * Created by samarthgupta on 13/02/17.
 */

public class UserInformation {

    private   String email;
    private String mno;
    private String city;
    private String fname;
    private String lname;
    private String icode;

    public UserInformation() {
        //Empty constructor
    }

    public UserInformation(String email , String fname, String lname, String mno, String city, String icode){

        this.email = email;
        this.city = city;
        this.fname = fname;
        this.lname = lname;
        this.mno = mno;
        this.icode = icode;


    }

    public String getEmail() {
        return email;
    }

    public String getIcode() {
        return icode;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getCity() {
        return city;
    }

    public String getMno() {
        return mno;
    }

}