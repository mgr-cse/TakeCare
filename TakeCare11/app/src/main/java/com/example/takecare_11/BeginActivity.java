package com.example.takecare_11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.takecare_11.signin.SignInActivity;
import com.example.takecare_11.signin.SignInToken;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.UserIDGetter;
import com.example.takecare_11.ui.chemist.ChemistDashboardActivity;
import com.example.takecare_11.ui.doctor.DoctorDashboardActivity;
import com.example.takecare_11.ui.pathologist.PathologistDashboardActivity;
import com.example.takecare_11.ui.patient.PatientDashboardActivity;

public class BeginActivity extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        SignInToken signInToken = new SignInToken(getApplicationContext());

        //key less sign in
        if(signInToken.loadToken())
        {
            String username = signInToken.getUsername();
            String password = signInToken.getPassword();
            try {

                id = new UserIDGetter(getApplicationContext(), username, password).execute().get();
                Integer.parseInt(id);   //if throws exception, not valid id

                String UserType = new FieldGetter(getApplicationContext(), id, "user_type", "users").execute().get();
                Intent intent;

                //start activity acc to user
                switch (UserType)
                {
                    case "patient":
                        intent = new Intent(this, PatientDashboardActivity.class);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        this.finish();
                        break;
                    case "doctor":
                        intent = new Intent(this, DoctorDashboardActivity.class);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        break;
                    case "chemist":
                        intent = new Intent(this, ChemistDashboardActivity.class);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        break;
                    case "patho":
                        intent = new Intent(this, PathologistDashboardActivity.class);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        break;
                    default: throw new Exception();
                }
                this.finish();

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Authentication Failure!", Toast.LENGTH_SHORT).show();
                signInToken.DeleteToken();

                startActivity(new Intent(this, SignInActivity.class));
                this.finish();
            }
        }
        else {
            //fresh sign in
            startActivity(new Intent(this, SignInActivity.class));
            this.finish();
        }
    }
}
