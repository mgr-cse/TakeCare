package com.example.takecare_11.ui.patient;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.patient.Prescription;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatientScanPrescriptionActivity extends AppCompatActivity {

    String id;

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    private class SavePrescription extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "patient/save_prescription.php" +
                    "?id=" + id +
                    "&json=" + strings[0];

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
        setContentView(R.layout.activity_patient_scan_prescription);

        //init
        id = getIntent().getStringExtra("user_id");
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.patient_qr_scan_status);
        surfaceView = findViewById(R.id.patient_scan_qr_surfaceview);
        btnAction = findViewById(R.id.patient_scan_qr_btn);
        btnAction.setEnabled(false);

        //set listener
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intentDataProcessing
                Gson gson = new Gson();
                try{
                    Prescription prescription = gson.fromJson(intentData, Prescription.class);
                    //save data here
                    String result = new SavePrescription().execute(intentData).get();
                    if(result.equals("success!!!"))
                        Toast.makeText(getApplicationContext(), "Prescription Saved!", Toast.LENGTH_SHORT).show();
                    else throw new Exception();

                    //terminate
                    PatientScanPrescriptionActivity.this.finish();
                }catch (JsonIOException e)
                {
                    Toast.makeText(getApplicationContext(), "QR code not Valid!", Toast.LENGTH_SHORT).show();
                    return;
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(PatientScanPrescriptionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(PatientScanPrescriptionActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            //get data
                            intentData = barcodes.valueAt(0).rawValue;

                            //first level verification
                            if(intentData.charAt(0)=='{')
                            {
                                btnAction.setEnabled(true);
                                txtBarcodeValue.setText("QR Found!");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
