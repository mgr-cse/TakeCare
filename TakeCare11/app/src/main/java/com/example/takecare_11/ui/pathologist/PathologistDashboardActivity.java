package com.example.takecare_11.ui.pathologist;

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
import com.example.takecare_11.ui.patient.PatientDashboardMyQRFragment;

import java.io.File;

public class PathologistDashboardActivity extends AppCompatActivity {

    //ui elements
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

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
                intent = new Intent(this, PathologistUpdateIDActivity.class);
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
        setContentView(R.layout.activity_pathologist_dashboard);

        //init
        id = getIntent().getStringExtra("user_id");
        toolbar = findViewById(R.id.patho_dashboard_toolbar);
        tabLayout = findViewById(R.id.patho_dashboard_tabs);
        fab = findViewById(R.id.patho_dashboard_add_prescription_fab);

        //toolbar
        setSupportActionBar(toolbar);

        //make bundle for id transport
        idBundle = new Bundle();
        idBundle.putString("user_id", id);

        //set default fragment
        PathologistQueueFragment pathologistQueueFragment = new PathologistQueueFragment();
        pathologistQueueFragment.setArguments(idBundle);
        ReplaceFragment(R.id.patho_dashboard_blank_fragment, pathologistQueueFragment,
                "patho_queue_fragment");

        //add tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabId = tab.getPosition();
                switch (tabId)
                {
                    case 0:
                        PathologistQueueFragment pathologistQueueFragment1 = new PathologistQueueFragment();
                        pathologistQueueFragment1.setArguments(idBundle);
                        ReplaceFragment(R.id.patho_dashboard_blank_fragment, pathologistQueueFragment1,
                                "patho_queue_fragment");
                        break;

                    case 1:
                        PatientDashboardMyQRFragment pathoQR = new PatientDashboardMyQRFragment();
                        pathoQR.setArguments(idBundle);
                        ReplaceFragment(R.id.patho_dashboard_blank_fragment, pathoQR,
                                "patho_dashboard_my_qr_fragment");
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        //set floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PathologistDashboardActivity.this, PathologistScanQRActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set default fragment
        PathologistQueueFragment pathologistQueueFragment2 = new PathologistQueueFragment();
        pathologistQueueFragment2.setArguments(idBundle);
        ReplaceFragment(R.id.patho_dashboard_blank_fragment, pathologistQueueFragment2,
                "patient_dashboard_prescription_fragment");
    }
}
