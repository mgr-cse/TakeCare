package com.example.takecare_11.storageclasses;

import android.content.Context;

import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.FieldSetter;

public class User {
    private String id;
    private Context context;

    //constructor
    public User(String id, Context context)
    {
        this.id = id;
        this.context = context;
    }

    //getters
    public String getFullName()
    {
        try {
            return new FieldGetter(context, id, "name", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getUsername()
    {
        try {
            return new FieldGetter(context, id, "username", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getPassword()
    {
        try {
            return new FieldGetter(context, id, "password", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getEmail()
    {
        try {
            return new FieldGetter(context, id, "email", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getMobile()
    {
        try {
            return new FieldGetter(context, id, "mobile", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getDob()
    {
        try {
            return new FieldGetter(context, id, "dob", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String getIDCard()
    {
        try {
            return new FieldGetter(context, id, "id_card", "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    //setters
    public String setFullName(String fullName)
    {
        try {
            return new FieldSetter(context, id, "name", fullName, "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String setPassword(String password)
    {
        try {
            return new FieldSetter(context, id, "password", password, "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String setEmail(String email)
    {
        try {
            return new FieldSetter(context, id, "email", email, "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public String setMobile(String mobile)
    {
        try {
            return new FieldSetter(context, id, "mobile", mobile, "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }

    public  String setIDCard(String idCard)
    {
        try {
            return new FieldSetter(context, id, "id_card", idCard, "users").execute().get();
        }catch (Exception e)
        {
            return "failure!!!";
        }
    }
}
