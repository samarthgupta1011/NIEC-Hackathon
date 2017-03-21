package com.samarthgupta.niec_hackathon.POJO;

/**
 * Created by samarthgupta on 21/03/17.
 */

public class UserAd {
    public String descrip;
    public String photo;
    public String condition;
    public String warranty;
    public int years;
    float costsp;

    public UserAd() {

    }


    public UserAd(String descrip, String photo, String condition, String warranty, int years) {
        this.descrip = descrip;
        this.photo = photo;
        this.condition = condition;
        this.warranty = warranty;
        this.years = years;

    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
}
