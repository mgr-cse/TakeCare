package com.example.takecare_11.ui.pathologist;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.DeleteRow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PathologistGiveReportActivity extends AppCompatActivity {

    String patientID;
    String queueID;
    String userID;

    EditText reportLink;
    Button giveReport, deleteReport;

    private class SaveReport extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "patient/save_report.php" +
                    "?id=" + patientID +
                    "&pathoid=" + userID +
                    "&link=" + strings[0];

            //make connection
            try{
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
                urlConnection.setRequestProperty("Cookie", "__test=2ad4957fd60a8b74775f11b309b4496a; expires=Friday, 1 January 2038 at 01:55:55; path=/");


                //read data
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String data = "";
                StringBuffer pageContent = new StringBuffer("");

                while ((data=br.readLine())!=null)
                    pageContent.append(data);
                return  pageContent.toString();

            }catch (Exception e)
            {
                return e.getMessage();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathologist_give_report);

        //init
        userID = getIntent().getStringExtra("user_id");
        patientID = getIntent().getStringExtra("patient_id");
        queueID = getIntent().getStringExtra("queue_id");

        reportLink = findViewById(R.id.patho_give_report_link);
        giveReport = findViewById(R.id.patho_give_report_btn);
        deleteReport = findViewById(R.id.patho_give_report_delete_btn);

        giveReport.setEnabled(false);

        reportLink.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(reportLink.getText().toString().length()>0)
                    giveReport.setEnabled(true);
            }
        });

        giveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String result = new SaveReport().execute(reportLink.getText().toString()).get();
                    if(result.equals("success!!!"))
                        new DeleteRow(getApplicationContext(), "patho_" + userID + "_queue", queueID).execute().get();
                    PathologistGiveReportActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String result = new DeleteRow(getApplicationContext(), "patho_" + userID + "_queue", queueID).execute().get();
                    PathologistGiveReportActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
