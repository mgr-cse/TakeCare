package com.example.takecare_11.ui.doctor;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.doctor.Doctor;
import com.example.takecare_11.storageclasses.patient.Prescription;
import com.google.gson.Gson;

public class DoctorMakePrescriptionActivity extends AppCompatActivity {

    //ui elements
    private EditText titleField, notesField;
    private Button givePrescription, addDose;

    private String id, doc;

    private int doseCount;

    private Prescription PrescriptionBuilder()
    {
        EditText title = findViewById(R.id.doctor_make_prescription_title);
        EditText notes = findViewById(R.id.doctor_make_prescription_notes);

        Prescription prescription = new Prescription(doc, title.getText().toString(), notes.getText().toString());
        prescription.setMaxDoseCount(doseCount);

        //set doses
        for(int i=0; i<doseCount; i++)
        {
            DoctorDoseFragment currDose = (DoctorDoseFragment) getSupportFragmentManager().findFragmentByTag("dose"+i);

            EditText med = currDose.getView().findViewById(R.id.doctor_dose_med_name);
            EditText days = currDose.getView().findViewById(R.id.doctor_dose_days);
            EditText qty = currDose.getView().findViewById(R.id.doctor_dose_qty);
            Spinner qtyType = currDose.getView().findViewById(R.id.doctor_dose_qty_type_spinner);
            CheckBox mor = currDose.getView().findViewById(R.id.doctor_dose_morning);
            CheckBox aft = currDose.getView().findViewById(R.id.doctor_dose_afternoon);
            CheckBox eve = currDose.getView().findViewById(R.id.doctor_dose_Evening);

            int daysInt, qtyInt;
            try
            {
                daysInt = Integer.parseInt(days.getText().toString());
            }catch (Exception e)
            {
                daysInt = 0;
            }
            try{
                qtyInt = Integer.parseInt(qty.getText().toString());
            }catch (Exception e)
            {
                qtyInt = 0;
            }

            //handle times here
            TextView timeCount = currDose.getView().findViewById(R.id.doctor_dose_time_count);
            int timeCountInt = Integer.parseInt(timeCount.getText().toString());

            int[] hrs = null;
            int[] min = null;

            if(timeCountInt>0)
            {
                hrs = new int[timeCountInt];
                min = new int[timeCountInt];

                for(int j=0; j<timeCountInt; j++)
                {
                    EditText hrsEt = currDose.getView().findViewWithTag("doseHrs"+j);
                    EditText minEt = currDose.getView().findViewWithTag("doseMin"+j);
                    try{
                        hrs[j] = Integer.parseInt(hrsEt.getText().toString());
                        min[j] = Integer.parseInt(minEt.getText().toString());
                    }catch (Exception e)
                    {
                        hrs[j] = 0;
                        min[j] = 0;
                    }
                }
            }

            prescription.addDose(
                    med.getText().toString(),
                    (String) qtyType.getSelectedItem(),
                    qtyInt,
                    daysInt,
                    mor.isChecked(), aft.isChecked(), eve.isChecked(),
                    timeCountInt, hrs, min
            );
        }
        return prescription;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_make_prescription);

        //init
        id = getIntent().getStringExtra("user_id");
        doc = new Doctor(id, getApplicationContext()).getFullName();
        if(doc.equals("failure!!!"))
        {
            Toast.makeText(getApplicationContext(), "Can't make prescription right now", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        doseCount = 0;

        titleField = findViewById(R.id.doctor_make_prescription_title);
        notesField = findViewById(R.id.doctor_make_prescription_notes);
        givePrescription = findViewById(R.id.doctor_make_prescription_give_btn);
        addDose = findViewById(R.id.doctor_give_prescription_add_dose_btn);

        //onClick listeners
        givePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.doctor_make_prescription_list, new DoctorDoseFragment(), "dose"+doseCount);
                doseCount++;
                ft.commit();
            }
        });

        givePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prescription prescription = PrescriptionBuilder();
                Gson gson = new Gson();
                String presString = gson.toJson(prescription, Prescription.class);

                Intent intent = new Intent(DoctorMakePrescriptionActivity.this, DoctorPrescriptionQRActivity.class);
                intent.putExtra("prescription", presString);
                startActivity(intent);
                DoctorMakePrescriptionActivity.this.finish();
            }
        });

    }
}
