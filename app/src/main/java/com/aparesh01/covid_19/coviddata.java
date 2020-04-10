package com.aparesh01.covid_19;

public class coviddata {

    String wcases,wdeaths,wrecovered,Icases,Ideaths,Irecovered;


    public coviddata(String wcases, String wdeaths, String wrecovered, String icases, String ideaths, String irecovered) {
        this.wcases = wcases;
        this.wdeaths = wdeaths;
        this.wrecovered = wrecovered;
        Icases = icases;
        Ideaths = ideaths;
        Irecovered = irecovered;
    }

    public coviddata() {

    }

    public String getWcases() {
        return wcases;
    }

    public void setWcases(String wcases) {
        this.wcases = wcases;
    }

    public String getWdeaths() {
        return wdeaths;
    }

    public void setWdeaths(String wdeaths) {
        this.wdeaths = wdeaths;
    }

    public String getWrecovered() {
        return wrecovered;
    }

    public void setWrecovered(String wrecovered) {
        this.wrecovered = wrecovered;
    }

    public String getIcases() {
        return Icases;
    }

    public void setIcases(String icases) {
        Icases = icases;
    }

    public String getIdeaths() {
        return Ideaths;
    }

    public void setIdeaths(String ideaths) {
        Ideaths = ideaths;
    }

    public String getIrecovered() {
        return Irecovered;
    }

    public void setIrecovered(String irecovered) {
        Irecovered = irecovered;
    }
}
