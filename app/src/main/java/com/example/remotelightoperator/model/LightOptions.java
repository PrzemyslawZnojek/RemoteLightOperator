package com.example.remotelightoperator.model;

import java.io.Serializable;

public class LightOptions implements Serializable {
    private int brightness;
    private int red;
    private int green;
    private int blue;

    public LightOptions(int brightness, int red, int green, int blue) {
        this.brightness = brightness;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public LightOptions() {
        this.brightness = 255;
        this.red = 255;
        this.green = 255;
        this.blue = 255;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
