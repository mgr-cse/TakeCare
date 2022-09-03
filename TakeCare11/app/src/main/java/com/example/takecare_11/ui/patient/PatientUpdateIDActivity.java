package com.example.takecare_11.ui.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.patient.MedicalID;
import com.example.takecare_11.storageclasses.patient.Patient;
import com.google.gson.Gson;

public class PatientUpdateIDActivity extends AppCompatActivity {

    //ui elements
    private EditText heightField, weightField, allergensField;
    private Spinner bloodGrpSpinner;
    private Button updateId;
    private MedicalID medicalID = null;

    String id;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_update_i_d);

        //init
        id = getIntent().getStringExtra("user_id");
        patient = new Patient(id, getApplicationContext());

        heightField = findViewById(R.id.update_patient_height);
        weightField = findViewById(R.id.update_patient_weight);
        allergensField = findViewById(R.id.update_patient_allergens);
        bloodGrpSpinner = findViewById(R.id.update_patient_bloodgrp_spinner);
        updateId = findViewById(R.id.update_patient_create_account);

        //add items to spinner
        String[] bloodGrps = new String[]
                {
                        "A", "AB", "B", "O"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bloodGrps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrpSpinner.setAdapter(adapter);

        String idJson = patient.getIDCard();
        try{
            Gson gson = new Gson();
            medicalID = gson.fromJson(idJson, MedicalID.class);

            //set current values
            heightField.setText(medicalID.getHeight());
            weightField.setText(medicalID.getWeight());
            allergensField.setText(medicalID.getAllergens());

            int spinnerPos = adapter.getPosition(medicalID.getBloodGrp());
            bloodGrpSpinner.setSelection(spinnerPos);

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            this.finish();
        }

        updateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicalID.setHeight(heightField.getText().toString());
                medicalID.setWeight(weightField.getText().toString());
                medicalID.setBloodGrp((String) bloodGrpSpinner.getSelectedItem());
                String test = allergensField.getText().toString();
                medicalID.setAllergens(allergensField.getText().toString());

                try{
                    Gson gson = new Gson();
                    String json = gson.toJson(medicalID, MedicalID.class);
                    patient.setIDCard(json);
                    PatientUpdateIDActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
