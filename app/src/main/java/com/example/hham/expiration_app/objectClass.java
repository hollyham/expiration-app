package com.example.hham.expiration_app;
import android.graphics.Picture;
import java.util.Date;


public class objectClass {
    String name = "";
    Date date = new Date();
    Picture picture = new Picture();
    public objectClass(String name, Date date, Picture picture){
        this.name = name;
        this.date = date;
        this.picture = picture;
    }

    public int getYear(){
        return this.date.getYear();
    }

    public void setDate(int year, int month, int day){
        this.date.setYear(year);
        this.date.setMonth(month);
        this.date.setDate(day);
    }

    public Date getDate(){
        return this.date;
    }

    public int getMonth(){
        return this.date.getMonth();
    }

    public int getDay(){
        return this.date.getDay();
    }

    public Picture getPicture(){
        return this.picture;
    }

    public String getName(){
        return this.name;
    }

}

