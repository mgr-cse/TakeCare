package com.example.takecare_11.storageclasses.patient;

import java.io.Serializable;

public class MedicalID implements Serializable
{
    private String height, weight, bloodGrp, allergens;

    //methods
    public MedicalID()
    {
        height = weight = bloodGrp = allergens = null;
    }

    //setters
    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    //getters
    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public String getAllergens() {
        return allergens;
    }
}
