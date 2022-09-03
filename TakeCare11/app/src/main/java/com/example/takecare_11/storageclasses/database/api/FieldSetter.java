package com.example.takecare_11.storageclasses.database.api;

import android.content.Context;
import android.os.AsyncTask;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FieldSetter extends AsyncTask<String, Integer, String>
{
    private String key, field, table, value;
    Context context;

    public FieldSetter(Context context, String key, String field, String value, String table)
    {
        this.key = key;
        this.field = field;
        this.value = value;
        this.table = table;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String host = ((MyApplication)context.getApplicationContext()).getHost();
        String link = host + "api/fieldsetter.php" +
                "?key=" + key +
                "&field=" + field +
                "&value=" + value +
                "&table=" + table;

        //make connection
        try{
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
            urlConnection.setRequestProperty("Cookie", "__test=2ad4957fd60a8b74775f11b309b4496a; expires=Friday, 1 January 2038 at 01:55:55; path=/");


            //read data
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String data = "";
            StringBuffer pageContent = new StringBuffer("");

            while ((data=br.readLine())!=null)
                pageContent.append(data);
            return  pageContent.toString();

        }catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
