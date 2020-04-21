package com.example.remotelightoperator.model;

public class LightOptions {
    private short brightness;
    private short red;
    private short green;
    private short blue;

    public LightOptions(short brightness, short red, short green, short blue) {
        this.brightness = brightness;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public short getBrightness() {
        return brightness;
    }

    public void setBrightness(short brightness) {
        this.brightness = brightness;
    }

    public short getRed() {
        return red;
    }

    public void setRed(short red) {
        this.red = red;
    }

    public short getGreen() {
        return green;
    }

    public void setGreen(short green) {
        this.green = green;
    }

    public short getBlue() {
        return blue;
    }

    public void setBlue(short blue) {
        this.blue = blue;
    }
}
