package com.example.takecare_11.ui.chemist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.chemist.Chemist;
import com.example.takecare_11.storageclasses.chemist.ChemistWorkID;
import com.example.takecare_11.storageclasses.patient.Patient;
import com.google.gson.Gson;

public class ChemistUpdateIDActivity extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, upiField;
    private Button updateId;

    String id;
    Chemist chemist;
    ChemistWorkID chemistWorkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_update_i_d);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.reg_chem_workplace_name);
        wAddField = findViewById(R.id.reg_chem_workplace_add);
        wMobileField = findViewById(R.id.reg_chem_work_phone);
        upiField = findViewById(R.id.reg_chem_upi_id);
        updateId = findViewById(R.id.reg_chem_create_account_btn);

        chemist = new Chemist(id, getApplicationContext());
        chemistWorkID = new ChemistWorkID();

        try{
            Gson gson = new Gson();
            chemistWorkID = gson.fromJson(chemist.getIDCard(), ChemistWorkID.class);

            //get current
            wPlaceField.setText(chemistWorkID.getWorkPlace());
            wAddField.setText(chemistWorkID.getWorkAddress());
            wMobileField.setText(chemistWorkID.getWorkMobile());
            upiField.setText(chemistWorkID.getUpi());
        }catch (Exception e)
        {
            this.finish();
        }

        updateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChemistWorkID chemistWorkID = new ChemistWorkID();
                chemistWorkID.setWorkPlace(wPlaceField.getText().toString());
                chemistWorkID.setWorkAddress(wAddField.getText().toString());
                chemistWorkID.setWorkMobile(wMobileField.getText().toString());
                chemistWorkID.setUpi(upiField.getText().toString());

                try{
                    Gson gson = new Gson();
                    String json = gson.toJson(chemistWorkID, ChemistWorkID.class);
                    chemist.setIDCard(json);

                    ChemistUpdateIDActivity.this.finish();

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
