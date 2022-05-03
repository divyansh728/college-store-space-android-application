package com.example.college_storespace;

public class Docxmodel {
    String flname,flurl;

    public Docxmodel(String flname, String flurl) {
        this.flname = flname;
        this.flurl = flurl;
    }

    public String getFlname() {
        return flname;
    }

    public void setFlname(String flname) {
        this.flname = flname;
    }

    public String getFlurl() {
        return flurl;
    }

    public void setFlurl(String flurl) {
        this.flurl = flurl;
    }
}
