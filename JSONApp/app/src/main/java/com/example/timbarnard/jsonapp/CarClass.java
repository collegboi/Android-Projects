package com.example.timbarnard.jsonapp;

/**
 * Created by Timbarnard on 10/10/2015.
 */
public class CarClass {

    private String name;
    private String model;
    private String reg;
    private String imageURL;

    public CarClass(String name, String model,String reg, String imageURL) {
        this.setName(name);
        this.setModel(model);
        this.setReg(reg);
        this.setImageURL(imageURL);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
