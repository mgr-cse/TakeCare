package com.example.takecare_11.ui.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.doctor.Doctor;
import com.example.takecare_11.storageclasses.doctor.DoctorWorkID;
import com.google.gson.Gson;

public class DoctorUpdateID extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, smcField;
    private Button updateId;
    private DoctorWorkID doctorWorkID;
    private Doctor doctor;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_update_i_d);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.update_doctor_workplace_name);
        wAddField = findViewById(R.id.update_doctor_workplace_address);
        wMobileField = findViewById(R.id.update_doctor_work_mobile);
        smcField = findViewById(R.id.update_doctor_smc);
        updateId = findViewById(R.id.update_doctor_create_account);

        doctor = new Doctor(id, getApplicationContext());
        doctorWorkID = new DoctorWorkID();

        //get current
        try{
            Gson gson = new Gson();
            doctorWorkID = gson.fromJson(doctor.getIDCard(), DoctorWorkID.class);

            wAddField.setText(doctorWorkID.getWorkAddress());
            wPlaceField.setText(doctorWorkID.getWorkPlace());
            wMobileField.setText(doctorWorkID.getWorkMobile());
            smcField.setText(doctorWorkID.getSmc());

        }catch (Exception e)
        {
            this.finish();
        }

        updateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorWorkID.setWorkPlace(wPlaceField.getText().toString());
                doctorWorkID.setWorkAddress(wAddField.getText().toString());
                doctorWorkID.setWorkMobile(wMobileField.getText().toString());
                doctorWorkID.setSmc(smcField.getText().toString());

                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(doctorWorkID, DoctorWorkID.class);
                    doctor.setIDCard(json);
                    DoctorUpdateID.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
