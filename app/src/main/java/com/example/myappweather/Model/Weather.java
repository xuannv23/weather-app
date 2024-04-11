package com.example.myappweather.Model;

public class Weather extends Future{
    private String cloudy;
    private String windySpeed;
    private String humidyty;
    private String tempTb;
    private String pressure;
    private String deg;
    //status, tempTB,
    public Weather(){};
    public Weather(String cloudy, String windySpeed, String humidyty, String tempTb, String pressure, String deg) {
        this.cloudy = cloudy;
        this.windySpeed = windySpeed;
        this.humidyty = humidyty;
        this.tempTb = tempTb;
        this.pressure = pressure;
        this.deg = deg;
    }

    public Weather(String day, String picPath, String status, int hightTemp, int lowTemp, String cloudy, String windySpeed, String humidyty, String tempTb, String pressure, String deg) {
        super(day, picPath, status, hightTemp, lowTemp);
        this.cloudy = cloudy;
        this.windySpeed = windySpeed;
        this.humidyty = humidyty;
        this.tempTb = tempTb;
        this.pressure = pressure;
        this.deg = deg;
    }

    public Weather(String status, String cloudy, String windySpeed, String humidyty, String tempTb, String pressure, String deg){
        super(status);
        this.cloudy = cloudy;
        this.windySpeed = windySpeed;
        this.humidyty = humidyty;
        this.tempTb = tempTb;
        this.pressure = pressure;
        this.deg = deg;
    }

    public String getCloudy() {
        return cloudy;
    }

    public void setCloudy(String cloudy) {
        this.cloudy = cloudy;
    }

    public String getWindySpeed() {
        return windySpeed;
    }

    public void setWindySpeed(String windySpeed) {
        this.windySpeed = windySpeed;
    }

    public String getHumidyty() {
        return humidyty;
    }

    public void setHumidyty(String humidyty) {
        this.humidyty = humidyty;
    }

    public String getTempTb() {
        return tempTb;
    }

    public void setTempTb(String tempTb) {
        this.tempTb = tempTb;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }
}
