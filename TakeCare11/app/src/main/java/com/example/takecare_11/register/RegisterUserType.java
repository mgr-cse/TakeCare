package com.example.takecare_11.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterUserType extends AppCompatActivity {
    //ui elements
    Button patientBtn, doctorBtn, chemistBtn, pathologistBtn;

    private String id;
    private String userType;

    //Async task
    private class Proceed extends AsyncTask<String, Integer, String>
    {

        Proceed(String userType){ RegisterUserType.this.userType = userType; }

        @Override
        protected String doInBackground(String... strings) {

            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "registerusertype.php" +
                    "?usertype=" + userType + "&id=" + id;

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

        @Override
        protected void onPostExecute(String result)
        {
            AfterSetUserType(result);
        }

    }

    private void AfterSetUserType(String result)
    {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        //proceed here
        Intent intent;
        switch (userType)
        {
            case "patient":
                intent = new Intent(this, RegisterPatientActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                break;
            case "doctor":
                intent = new Intent(this,RegisterDoctorActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                break;
            case "chemist":
                intent = new Intent(this,RegisterChemistActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                break;
            case "patho":
                intent = new Intent(this,RegisterPathologistActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                break;
            default: return;
        }

        this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_type);

        //init
        patientBtn = findViewById(R.id.register_user_type_patient_btn);
        doctorBtn = findViewById(R.id.register_user_type_doctor_btn);
        chemistBtn = findViewById(R.id.register_user_type_chemist_btn);
        pathologistBtn = findViewById(R.id.register_user_type_pathologist_btn);

        id = getIntent().getStringExtra("user_id");

        //set onClick listeners
        patientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Proceed("patient").execute();
            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Proceed("doctor").execute();
            }
        });

        chemistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Proceed("chemist").execute();
            }
        });

        pathologistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Proceed("patho").execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
