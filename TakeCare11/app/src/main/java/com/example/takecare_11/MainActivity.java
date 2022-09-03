package com.example.takecare_11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.signin.SignInActivity;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.UserIDGetter;
import com.example.takecare_11.storageclasses.pathologist.Pathologist;
import com.example.takecare_11.ui.chemist.ChemistDashboardActivity;
import com.example.takecare_11.ui.doctor.DoctorDashboardActivity;
import com.example.takecare_11.ui.pathologist.PathologistDashboardActivity;
import com.example.takecare_11.ui.patient.PatientDashboardActivity;

public class MainActivity extends AppCompatActivity {

    EditText ipField;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipField = findViewById(R.id.IP_text_field);
        nextBtn = findViewById(R.id.IP_proceed_btn);

        ipField.setText("13.232.188.187");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ipField.getText().toString();

                if(!url.startsWith("http://"))
                    url = "http://" + url;
                if(!url.endsWith("/"))
                    url = url + "/";

                url = url + "TakeCare/";
                ((MyApplication)MainActivity.this.getApplication())
                        .setHost(url);

                Intent intent = new Intent(MainActivity.this, BeginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
}
