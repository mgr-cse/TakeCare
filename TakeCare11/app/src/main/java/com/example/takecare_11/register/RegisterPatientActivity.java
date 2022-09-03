package com.example.takecare_11.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.patient.MedicalID;
import com.example.takecare_11.storageclasses.patient.Patient;
import com.example.takecare_11.ui.patient.PatientDashboardActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterPatientActivity extends AppCompatActivity {

    //ui elements
    private EditText heightField, weightField, allergensField;
    private Spinner bloodGrpSpinner;
    private Button createAccountBtn;

    String id;

    //Async Tasks
    private class Proceed extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {

            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "registeridcard.php" +
                    "?id=" + id +
                    "&json=" + strings[0];

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
        protected void onPostExecute(String result) {
            AfterCreateAccount(result);
        }
    }

    private class CreatePrescriptionTable extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "patient/create_prescription_table.php" +
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

        @Override
        protected void onPostExecute(String result) {
            AfterCreateTable(result);
        }
    }

    private void AfterCreateAccount(String result)
    {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        new CreatePrescriptionTable().execute();
    }

    private void AfterCreateTable(String result)
    {
        Toast.makeText(getApplicationContext(), "Table create:" + result, Toast.LENGTH_LONG);

        try {
            Patient patient = new Patient(id, getApplicationContext());
            String username = patient.getUsername();
            String password = patient.getPassword();

            new SignInToken(getApplicationContext()).CreateToken(username, password);

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }

        //termintate this, start next activity
        Intent intent = new Intent(this, PatientDashboardActivity.class);
        intent.putExtra("user_id", id);
        startActivity(intent);
        this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        //init
        id = getIntent().getStringExtra("user_id");
        heightField = findViewById(R.id.reg_patient_height);
        weightField = findViewById(R.id.reg_patient_weight);
        allergensField = findViewById(R.id.reg_patient_allergens);
        bloodGrpSpinner = findViewById(R.id.reg_patient_bloodgrp_spinner);
        createAccountBtn = findViewById(R.id.reg_patient_create_account);

        //add items to spinner
        String[] bloodGrps = new String[]
                {
                        "A", "AB", "B", "O"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bloodGrps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrpSpinner.setAdapter(adapter);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicalID medicalID = new MedicalID();
                medicalID.setHeight(heightField.getText().toString());
                medicalID.setWeight(weightField.getText().toString());
                medicalID.setBloodGrp((String) bloodGrpSpinner.getSelectedItem());
                medicalID.setAllergens(allergensField.getText().toString());

                Gson gson = new Gson();
                String medicalIDJson = gson.toJson(medicalID, MedicalID.class);

                new Proceed().execute(medicalIDJson);

            }
        });
    }

}
