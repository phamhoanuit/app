package com.example.firebaseexample.model;

public class Coffee {
    private String nameCoffee;
    private double price;
    private String linkImage;

    public Coffee() {
    }

    public Coffee(String nameCoffee, double price, String linkImage) {
        this.nameCoffee = nameCoffee;
        this.price = price;
        this.linkImage = linkImage;
    }

    public String getnameCoffee() {
        return nameCoffee;
    }

    public void setnameCoffee(String nameCoffee) {
        this.nameCoffee = nameCoffee;
    }

    public double getprice() {
        return price;
    }

    public void setprice(double price) {
        this.price = price;
    }

    public String getlinkImage() {
        return linkImage;
    }

    public void setlinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
}
