package com.example.takecare_11.ui.doctor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInActivity;
import com.example.takecare_11.ui.UpdateAccountActivity;

import java.io.File;

public class DoctorDashboardActivity extends AppCompatActivity {

    //ui elements
    private TabLayout tabLayout;
    private Toolbar toolbar;

    //id bundle
    Bundle idBundle;

    String id;

    private void ReplaceFragment(int id, Fragment fragment, String tag)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, tag);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add menu
        getMenuInflater().inflate(R.menu.menu_dashboard_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.menu_dashboard_item_logout:
                File file = new File(getFilesDir(), "signin_token.txt");
                file.delete();
                startActivity(new Intent(this, SignInActivity.class));
                this.finish();
                return true;

            case R.id.menu_dashboard_item_update_account:
                intent = new Intent(this, UpdateAccountActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                return true;

            case R.id.menu_dashboard_item_update_id:
                intent = new Intent(this, DoctorUpdateID.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        //init
        id= getIntent().getStringExtra("user_id");
        toolbar = findViewById(R.id.doctor_dashboard_toolbar);
        tabLayout = findViewById(R.id.doctor_dashboard_tabs);

        //toolbar
        setSupportActionBar(toolbar);

        //make bundle for id transport
        idBundle = new Bundle();
        idBundle.putString("user_id", id);

        //set default fragment
        DoctorDashboardMakePrescriptionFragment docDashPres = new DoctorDashboardMakePrescriptionFragment();
        docDashPres.setArguments(idBundle);
        ReplaceFragment(R.id.doctor_dashboard_blank_fragment, docDashPres,
                "doctor_dashboard_prescription_fragment");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabId = tab.getPosition();
                switch (tabId)
                {
                    case 0:
                        DoctorDashboardMakePrescriptionFragment docDashPres = new DoctorDashboardMakePrescriptionFragment();
                        docDashPres.setArguments(idBundle);
                        ReplaceFragment(R.id.doctor_dashboard_blank_fragment, docDashPres,
                                "doctor_dashboard_prescription_fragment");
                        break;
                    case 1:
                        DoctorDashboardAppointmentsFragment docAppoint = new DoctorDashboardAppointmentsFragment();
                        docAppoint.setArguments(idBundle);
                        ReplaceFragment(R.id.doctor_dashboard_blank_fragment, docAppoint,
                                "doctor_dashboard_appointments_fragment");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });



    }
}
