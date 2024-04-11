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

}
