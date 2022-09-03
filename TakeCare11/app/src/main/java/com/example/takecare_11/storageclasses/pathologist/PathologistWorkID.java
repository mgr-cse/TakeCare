package com.example.takecare_11.storageclasses.pathologist;

import java.io.Serializable;

public class PathologistWorkID implements Serializable
{
    private String workPlace, workAddress, workMobile, upi;

    public PathologistWorkID()
    {
        workPlace = workAddress = workMobile = upi = null;
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

    public void setUpi(String smc) {
        this.upi = smc;
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

    public String getUpi() {
        return upi;
    }
}
