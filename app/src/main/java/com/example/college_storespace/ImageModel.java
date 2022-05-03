package com.example.college_storespace;

public class ImageModel {
    String imgdata;
    String image;

    public ImageModel() {
    }

    public ImageModel(String imgdata, String image) {
        this.imgdata = imgdata;
        this.image = image;
    }

    public String getImgdata() {
        return imgdata;
    }

    public void setImgdata(String imgdata) {
        this.imgdata = imgdata;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
