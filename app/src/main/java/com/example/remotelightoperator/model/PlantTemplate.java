package com.example.remotelightoperator.model;

import java.io.Serializable;

public class PlantTemplate implements Serializable {
    private int plantID;
    private String name;
    private String description;
    private int irradiationTime;
    private float rate;
    private float rateCount;

    public PlantTemplate() {}

    public PlantTemplate(int plantID, String name, String description, int irradiationTime, float rate, float rateCount) {
        this.plantID = plantID;
        this.name = name;
        this.description = description;
        this.irradiationTime = irradiationTime;
        this.rate = rate;
        this.rateCount = rateCount;
    }

    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIrradiationTime() {
        return irradiationTime;
    }

    public void setIrradiationTime(int irradiationTime) {
        this.irradiationTime = irradiationTime;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRateCount() {
        return rateCount;
    }

    public void setRateCount(float rateCount) {
        this.rateCount = rateCount;
    }

    @Override
    public String toString() {
        return "PlantTemplate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", irradiationTime=" + irradiationTime +
                ", rate=" + rate +
                ", rateCount=" + rateCount +
                '}';
    }
}
