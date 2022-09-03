package com.example.takecare_11.storageclasses.doctor;

import java.io.Serializable;

public class DoctorWorkID implements Serializable
{
    private String workPlace, workAddress, workMobile, smc;

    public DoctorWorkID()
    {
        workPlace = workAddress = workMobile = smc = null;
    }

    //setters
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public void setWorkMobile(String workMobile) {
        this.workMobile = workMobile;
    }

    public void setSmc(String smc) {
        this.smc = smc;
    }

    //getters

    public String getWorkPlace() {
        return workPlace;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public String getWorkMobile() {
        return workMobile;
    }

    public String getSmc() {
        return smc;
    }
}
