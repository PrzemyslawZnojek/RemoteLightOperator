package com.example.remotelightoperator.plantchooser;


public final class Plant {
    private final String name;
    private final String description;
    private final int irradiationTime;
    private final String creatorID;
    private final float number;
    private final int rateCount;

    public Plant(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.irradiationTime = builder.irradiationTime;
        this.creatorID = builder.creatorID;
        this.number = builder.number;
        this.rateCount = builder.rateCount;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIrradiationTime() {
        return irradiationTime;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public float getNumber() {
        return number;
    }

    public int getRateCount() {
        return rateCount;
    }

    public static class Builder{
        private String name;
        private String description;
        private int irradiationTime;
        private String creatorID;
        private float number;
        private int rateCount;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withDescription(String description){
            this.description = description;
            return this;
        }

        public Builder withName(int irradiationTime){
            this.irradiationTime = irradiationTime;
            return this;
        }

        public Builder withCreatorID(String creatorID){
            this.creatorID = creatorID;
            return this;
        }

        public Builder withNumber(float number){
            this.number = number;
            return this;
        }

        public Builder withRateCount(int rateCount){
            this.rateCount = rateCount;
            return this;
        }

        public Plant build(){
            return new Plant(this);
        }
    }


}
