package com.example.takecare_11.ui.pathologist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.pathologist.Pathologist;
import com.example.takecare_11.storageclasses.pathologist.PathologistWorkID;
import com.google.gson.Gson;

public class PathologistUpdateIDActivity extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, upiField;
    private Button updateId;

    private String id;

    private Pathologist pathologist;
    private PathologistWorkID pathologistWorkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathologist_update_i_d);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.update_patho_workplace_name);
        wAddField = findViewById(R.id.update_patho_workplace_add);
        wMobileField = findViewById(R.id.update_patho_work_phone);
        upiField = findViewById(R.id.update_patho_upi_id);
        updateId = findViewById(R.id.update_patho_create_account_btn);

        pathologist = new Pathologist(id, getApplicationContext());
        pathologistWorkID = new PathologistWorkID();

        try{
            Gson gson = new Gson();
            pathologistWorkID = gson.fromJson(pathologist.getIDCard(), PathologistWorkID.class);

            //get current data
            wPlaceField.setText(pathologistWorkID.getWorkPlace());
            wAddField.setText(pathologistWorkID.getWorkAddress());
            wMobileField.setText(pathologistWorkID.getWorkMobile());
            upiField.setText(pathologistWorkID.getUpi());

        }catch (Exception e)
        {
            this.finish();
        }

        updateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathologistWorkID.setWorkPlace(wPlaceField.getText().toString());
                pathologistWorkID.setWorkAddress(wAddField.getText().toString());
                pathologistWorkID.setWorkMobile(wMobileField.getText().toString());
                pathologistWorkID.setUpi(upiField.getText().toString());

                try{
                    Gson gson = new Gson();
                    String json = gson.toJson(pathologistWorkID, PathologistWorkID.class);
                    pathologist.setIDCard(json);
                    PathologistUpdateIDActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
