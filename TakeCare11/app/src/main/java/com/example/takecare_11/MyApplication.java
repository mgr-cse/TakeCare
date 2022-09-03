package com.example.takecare_11;

import android.app.Application;

public class MyApplication extends Application {
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
