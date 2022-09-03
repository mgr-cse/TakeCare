package com.example.takecare_11.register;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.pathologist.Pathologist;
import com.example.takecare_11.storageclasses.pathologist.PathologistWorkID;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class RegisterPathologistActivity extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, upiField;
    private Button createAccount;

    String id;

    private class Proceed extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "patho/create_user_queue_table.php" +
                    "?id=" + id;

            //make connection
            try{
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pathologist);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.reg_patho_workplace_name);
        wAddField = findViewById(R.id.reg_patho_workplace_add);
        wMobileField = findViewById(R.id.reg_patho_work_phone);
        upiField = findViewById(R.id.reg_patho_upi_id);
        createAccount = findViewById(R.id.reg_patho_create_account_btn);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathologistWorkID pathologistWorkID = new PathologistWorkID();
                pathologistWorkID.setWorkPlace(wPlaceField.getText().toString());
                pathologistWorkID.setWorkAddress(wAddField.getText().toString());
                pathologistWorkID.setWorkMobile(wMobileField.getText().toString());
                pathologistWorkID.setUpi(upiField.getText().toString());

                Gson gson = new Gson();
                String pathologistWorkIDjson = gson.toJson(pathologistWorkID, PathologistWorkID.class);

                Pathologist pathologist = new Pathologist(id, getApplicationContext());

                String result = pathologist.setIDCard(pathologistWorkIDjson);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                try{
                    String resultTable = new Proceed().execute().get();
                    if(resultTable.equals("failure"))
                        throw new Exception();

                    String username = pathologist.getUsername();
                    String password = pathologist.getPassword();

                    new SignInToken(getApplicationContext()).CreateToken(username, password);

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }

                RegisterPathologistActivity.this.finish();

            }
        });
    }
}
