package com.demomvvm;

public class Singleton {


    String site_color="#c4ccfc",site_bgcolor="#c4ccfc";


    public String getSite_color() {
        return site_color;
    }

    public void setSite_color(String site_color) {
        this.site_color = site_color;
    }

    public String getSite_bgcolor() {
        return site_bgcolor;
    }

    public void setSite_bgcolor(String site_bgcolor) {
        this.site_bgcolor = site_bgcolor;
    }

    private static Singleton instance = null;

    private Singleton(){
    }

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
    }

    public static Singleton getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

}
