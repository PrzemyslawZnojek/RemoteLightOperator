package com.example.remotelightoperator.model;

import java.io.Serializable;
import java.util.Objects;

public class UserConfiguration implements Serializable {
    private String uid;
    private int irradiationTime;
    private ForcedState forcedState;
    private String plantName;
    private String description;
    private LightOptions lightOptions;

    public LightOptions getLightOptions() {
        return lightOptions;
    }

    public void setLightOptions(LightOptions lightOptions) {
        this.lightOptions = lightOptions;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIrradiationTime() {
        return irradiationTime;
    }

    public void setIrradiationTime(int irradiationTime) {
        this.irradiationTime = irradiationTime;
    }

    public ForcedState getForcedState() {
        return forcedState;
    }

    public void setForcedState(ForcedState forcedState) {
        this.forcedState = forcedState;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConfiguration that = (UserConfiguration) o;
        return irradiationTime == that.irradiationTime &&
                uid.equals(that.uid) &&
                forcedState == that.forcedState &&
                Objects.equals(plantName, that.plantName) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, irradiationTime, forcedState, plantName, description);
    }
}
