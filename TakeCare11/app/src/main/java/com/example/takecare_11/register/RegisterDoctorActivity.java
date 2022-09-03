package com.example.takecare_11.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.doctor.Doctor;
import com.example.takecare_11.storageclasses.doctor.DoctorWorkID;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

public class RegisterDoctorActivity extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, smcField;
    private Button createAccount;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.reg_doctor_workplace_name);
        wAddField = findViewById(R.id.reg_doctor_workplace_address);
        wMobileField = findViewById(R.id.reg_doctor_work_mobile);
        smcField = findViewById(R.id.reg_doctor_smc);
        createAccount = findViewById(R.id.reg_doctor_create_account);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorWorkID doctorWorkID = new DoctorWorkID();

                doctorWorkID.setWorkPlace(wPlaceField.getText().toString());
                doctorWorkID.setWorkAddress(wAddField.getText().toString());
                doctorWorkID.setWorkMobile(wMobileField.getText().toString());
                doctorWorkID.setSmc(smcField.getText().toString());

                Gson gson = new Gson();
                String doctorWorkIDjson = gson.toJson(doctorWorkID, DoctorWorkID.class);

                Doctor doctor = new Doctor(id, getApplicationContext());

                String result = doctor.setIDCard(doctorWorkIDjson);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                try{
                    String username = doctor.getUsername();
                    String password = doctor.getPassword();

                    new SignInToken(getApplicationContext()).CreateToken(username, password);

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }

                RegisterDoctorActivity.this.finish();
            }
        });
    }
}
