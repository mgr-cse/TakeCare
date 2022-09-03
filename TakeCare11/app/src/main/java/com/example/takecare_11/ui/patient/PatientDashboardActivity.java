package com.example.takecare_11.ui.patient;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInActivity;
import com.example.takecare_11.ui.UpdateAccountActivity;

import java.io.File;

public class PatientDashboardActivity extends AppCompatActivity {

    //ui elements
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private int tabId;

    private String id;

    //id bundle
    Bundle idBundle;

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
                intent = new Intent(this, PatientUpdateIDActivity.class);
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
        setContentView(R.layout.activity_patient_dashboard);

        //init
        id = getIntent().getStringExtra("user_id");
        toolbar = findViewById(R.id.patient_dashboard_toolbar);
        tabLayout = findViewById(R.id.patient_dashboard_tabs);
        fab = findViewById(R.id.patient_dashboard_add_prescription_fab);

        //toolbar
        setSupportActionBar(toolbar);

        //make bundle for id transport
        idBundle = new Bundle();
        idBundle.putString("user_id", id);

        //set default fragment
        PatientDashboardPrescriptionFragment patientDashPres = new PatientDashboardPrescriptionFragment();
        patientDashPres.setArguments(idBundle);
        ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientDashPres,
                "patient_dashboard_prescription_fragment");

        //add tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabId = tab.getPosition();
                switch (tabId)
                {
                    case 0:
                        PatientDashboardPrescriptionFragment patientDashPres = new PatientDashboardPrescriptionFragment();
                        patientDashPres.setArguments(idBundle);
                        ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientDashPres,
                                "patient_dashboard_prescription_fragment");
                        break;
                    case 1:
                        PatientDashboardMyQRFragment patientQR = new PatientDashboardMyQRFragment();
                        patientQR.setArguments(idBundle);
                        ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientQR,
                                "patient_dashboard_my_qr_fragment");
                        break;
                    case 2:
                        PatientDashboardReportFragment patientReport = new PatientDashboardReportFragment();
                        patientReport.setArguments(idBundle);
                        ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientReport,
                                "patient_dashboard_report_fragment");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        //set floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboardActivity.this, PatientScanPrescriptionActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set default fragment
        switch (tabId)
        {
            case 0:
                PatientDashboardPrescriptionFragment patientDashPres = new PatientDashboardPrescriptionFragment();
                patientDashPres.setArguments(idBundle);
                ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientDashPres,
                        "patient_dashboard_prescription_fragment");
                break;
            case 1:
                PatientDashboardMyQRFragment patientQR = new PatientDashboardMyQRFragment();
                patientQR.setArguments(idBundle);
                ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientQR,
                        "patient_dashboard_my_qr_fragment");
                break;
            case 2:
                PatientDashboardReportFragment patientReport = new PatientDashboardReportFragment();
                patientReport.setArguments(idBundle);
                ReplaceFragment(R.id.patient_dashboard_blank_fragment, patientReport,
                        "patient_dashboard_report_fragment");
                break;
        }
    }
}
