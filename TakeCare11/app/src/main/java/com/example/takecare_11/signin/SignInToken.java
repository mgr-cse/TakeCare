package com.example.takecare_11.signin;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SignInToken {
    private Context context;
    private String m_username, m_password;

    public SignInToken(Context context)
    {
        this.context = context;
        m_username = null;
        m_password = null;
    }

    public boolean CreateToken(String username, String password)
    {
        try {
            File path = context.getFilesDir();
            File file = new File(path, "signin_token.txt");

            FileOutputStream fos = new FileOutputStream(file);
            fos.write((username + "\n" + password + "\n").getBytes());
            fos.close();
            return  true;

        }catch (Exception e)
        {
            return false;
        }
    }

    public boolean loadToken()
    {
        try{
            FileInputStream fis = context.openFileInput("signin_token.txt");
            InputStreamReader isr =  new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            m_username = br.readLine();
            m_password = br.readLine();

            br.close();
            isr.close();
            fis.close();

            return true;

        }catch (FileNotFoundException noFile)
        {
            return  false;
        }catch (IOException readError)
        {
            //malformed token
            DeleteToken();
            return  false;
        }
    }

    public boolean DeleteToken()
    {
        try{
            File path = context.getFilesDir();
            File file = new File(path, "signin_token.txt");
            file.delete();
            return true;
        }catch (Exception e)
        {
            return  false;
        }
    }

    //getters
    public String getUsername()
    {
        return m_username;
    }

    public String getPassword()
    {
        return m_password;
    }



}
