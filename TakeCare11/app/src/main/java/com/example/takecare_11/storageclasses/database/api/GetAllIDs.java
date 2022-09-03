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
import java.util.zip.Inflater;

public class GetAllIDs extends AsyncTask<String, Integer, Integer[]>
{
    private String table;
    private Context context;

    public GetAllIDs(Context context, String table)
    {
        this.context = context;
        this.table = table;
    }

    @Override
    protected Integer[] doInBackground(String... strings) {
        String host = ((MyApplication)context.getApplicationContext()).getHost();
        String link = host + "api/get_all_ids.php" +
                "?table=" + table;

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

            data=br.readLine();

            int count = Integer.parseInt(data);

            Integer[] ids = new Integer[count+1];
            ids[0] = count;

            for(int i=1; i<=count; i++) {
                data = br.readLine();
                ids[i] = Integer.parseInt(data);
            }
            return ids;

        }catch (Exception e)
        {
            return null;
        }
    }
}
