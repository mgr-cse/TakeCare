package com.example.takecare_11.ui.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.DeleteRow;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.patient.Patient;
import com.example.takecare_11.storageclasses.patient.Prescription;
import com.google.gson.Gson;

public class PatientViewPrescriptionActivity extends AppCompatActivity {

    private String id;
    private String userId;
    String presJson;
    private Prescription prescription;

    //ui
    private TextView presTitle, docName, presNotes;
    LinearLayout doseList;
    private Button presBuy, presDelete, presAlarm, presDeleteAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_prescription);

        id = getIntent().getStringExtra("prescription_id");
        presJson = getIntent().getStringExtra("prescription");
        prescription = new Gson().fromJson(presJson, Prescription.class);
        userId = getIntent().getStringExtra("user_id");

        //init
        presTitle = findViewById(R.id.patient_view_prescription_title);
        docName = findViewById(R.id.patient_view_prescription_doctor_name);
        presNotes = findViewById(R.id.patient_view_prescription_notes);
        doseList = findViewById(R.id.patient_view_prescription_doses);
        presDelete = findViewById(R.id.patient_view_prescription_delete);
        presAlarm = findViewById(R.id.patient_view_prescription_setAlarm);
        presDeleteAlarm = findViewById(R.id.patient_view_pescriptin_delete_alarm);
        presBuy = findViewById(R.id.patient_view_prescription_buy);


        presTitle.setText(prescription.getTitle());
        docName.setText(prescription.getDocName());
        presNotes.setText(prescription.getNotes());

        prescription.addDosesToLinearLayout(getLayoutInflater(), doseList);

        presDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new DeleteRow(getApplicationContext(), "patient_" + userId + "_prescriptions", id).execute().get();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                PatientViewPrescriptionActivity.this.finish();
            }
        });

        presAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescription.SetAlarms(PatientViewPrescriptionActivity.this, Integer.parseInt(id));
            }
        });

        presDeleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescription.CancelAllAlarms(PatientViewPrescriptionActivity.this, Integer.parseInt(id));
            }
        });
        try {
            String buyCheck;
            buyCheck = new FieldGetter(getApplicationContext(), id, "buy", "patient_" + userId + "_prescriptions").execute().get();
            if(buyCheck.equals("1"))
                presBuy.setEnabled(false);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        presBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //patient buy activity
                Intent intent = new Intent(PatientViewPrescriptionActivity.this, PatientBuyPrescriptionActivity.class);
                intent.putExtra("prescription_id", id);
                intent.putExtra("user_id", userId);
                startActivity(intent);

            }
        });

    }
}
