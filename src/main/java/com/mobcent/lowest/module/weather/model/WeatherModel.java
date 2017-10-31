package com.mobcent.lowest.module.weather.model;

import com.mobcent.lowest.base.model.BaseModel;

public class WeatherModel extends BaseModel {
    private static final long serialVersionUID = 7468391918123623681L;
    private String city;
    private String date;
    private String dressIndex;
    private String dressSuggest;
    private int picInfo;
    private String temperature;
    private String uVIndex;
    private String washCarIndex;
    private String weather;
    private String week;
    private String wind;

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getPicInfo() {
        return this.picInfo;
    }

    public void setPicInfo(int picInfo) {
        this.picInfo = picInfo;
    }

    public String getWind() {
        return this.wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getDressIndex() {
        return this.dressIndex;
    }

    public void setDressIndex(String dressIndex) {
        this.dressIndex = dressIndex;
    }

    public String getDressSuggest() {
        return this.dressSuggest;
    }

    public void setDressSuggest(String dressSuggest) {
        this.dressSuggest = dressSuggest;
    }

    public String getuVIndex() {
        return this.uVIndex;
    }

    public void setuVIndex(String uVIndex) {
        this.uVIndex = uVIndex;
    }

    public String getWashCarIndex() {
        return this.washCarIndex;
    }

    public void setWashCarIndex(String washCarIndex) {
        this.washCarIndex = washCarIndex;
    }

    public String toString() {
        return "WeatherModel [date=" + this.date + ", week=" + this.week + ", temperature=" + this.temperature + ", weather=" + this.weather + ", picInfo=" + this.picInfo + ", wind=" + this.wind + ", dressIndex=" + this.dressIndex + ", dressSuggest=" + this.dressSuggest + ", uVIndex=" + this.uVIndex + ", washCarIndex=" + this.washCarIndex + "]";
    }
}
