package com.example.takecare_11.ui.patient;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.User;
import com.example.takecare_11.storageclasses.database.api.DeleteRow;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;

public class PatientViewReportActivity extends AppCompatActivity {

    private String userID, pathoID, reportID;

    private TextView linkTv;
    private Button deleteReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_report);

        //init
        userID = getIntent().getStringExtra("user_id");
        pathoID = getIntent().getStringExtra("patho_id");
        reportID = getIntent().getStringExtra("report_id");

        linkTv = findViewById(R.id.patient_view_report_link);
        deleteReport = findViewById(R.id.patient_view_report_delete);

        String reportLink = "";
        try{
               reportLink = new FieldGetter(getApplicationContext(), reportID, "link", "patient_"+userID+"_reports").execute().get();
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        linkTv.setClickable(true);
        //linkTv.setMovementMethod(LinkMovementMethod.getInstance());
        String text = reportLink;
        linkTv.setText(text);

        deleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String result = new DeleteRow(getApplicationContext(), "patient_"+userID+"_reports", reportID).execute().get();
                    if(result.equals("success!!!"))
                        PatientViewReportActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
