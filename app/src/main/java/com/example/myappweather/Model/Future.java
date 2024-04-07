package com.example.myappweather.Model;

public class Future {
    private String day;
    private int picPath;
    private String status;
    private int hightTemp;
    private int lowTemp;

    public Future(String day, int picPath, String status, int hightTemp, int lowTemp) {
        this.day = day;
        this.picPath = picPath;
        this.status = status;
        this.hightTemp = hightTemp;
        this.lowTemp = lowTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getPicPath() {
        return picPath;
    }

    public void setPicPath(int picPath) {
        this.picPath = picPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHightTemp() {
        return hightTemp;
    }

    public void setHightTemp(int hightTemp) {
        this.hightTemp = hightTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }
}
