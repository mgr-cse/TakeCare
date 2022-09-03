package com.example.takecare_11.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.chemist.Chemist;
import com.example.takecare_11.storageclasses.chemist.ChemistWorkID;
import com.google.gson.Gson;

public class RegisterChemistActivity extends AppCompatActivity {

    private EditText wPlaceField, wAddField, wMobileField, upiField;
    private Button createAccount;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_chemist);

        //init
        id = getIntent().getStringExtra("user_id");
        wPlaceField = findViewById(R.id.reg_chem_workplace_name);
        wAddField = findViewById(R.id.reg_chem_workplace_add);
        wMobileField = findViewById(R.id.reg_chem_work_phone);
        upiField = findViewById(R.id.reg_chem_upi_id);
        createAccount = findViewById(R.id.reg_chem_create_account_btn);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChemistWorkID chemistWorkID = new ChemistWorkID();
                chemistWorkID.setWorkPlace(wPlaceField.getText().toString());
                chemistWorkID.setWorkAddress(wAddField.getText().toString());
                chemistWorkID.setWorkMobile(wMobileField.getText().toString());
                chemistWorkID.setUpi(upiField.getText().toString());

                Gson gson = new Gson();
                String chemistWorkIDjson = gson.toJson(chemistWorkID, ChemistWorkID.class);

                Chemist chemist = new Chemist(id, getApplicationContext());

                String result = chemist.setIDCard(chemistWorkIDjson);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                try{
                    String username = chemist.getUsername();
                    String password = chemist.getPassword();

                    new SignInToken(getApplicationContext()).CreateToken(username, password);

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }

                RegisterChemistActivity.this.finish();

            }
        });
    }
}
