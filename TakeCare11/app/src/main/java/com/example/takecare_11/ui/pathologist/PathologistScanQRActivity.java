package com.example.takecare_11.ui.pathologist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import com.example.takecare_11.ui.patient.PatientScanPrescriptionActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PathologistScanQRActivity extends AppCompatActivity {

    String id;
    int patID;

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    private class SavePatient extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();
            String link = host + "patho/save_patient.php" +
                    "?id=" + id +
                    "&patid=" + strings[0];

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
        setContentView(R.layout.activity_pathologist_scan_qr);

        //init
        id = getIntent().getStringExtra("user_id");
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.patho_qr_scan_status);
        surfaceView = findViewById(R.id.patho_scan_qr_surfaceview);
        btnAction = findViewById(R.id.patho_scan_qr_btn);
        btnAction.setEnabled(false);

        //set listener
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intentDataProcessing
                try {
                    String result = new SavePatient().execute(String.valueOf(patID)).get();
                    if(result.equals("failure!!!"))
                        throw new Exception();
                    PathologistScanQRActivity.this.finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    if (ActivityCompat.checkSelfPermission(PathologistScanQRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(PathologistScanQRActivity.this, new
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
                            String check = intentData.substring(0, 9);
                            //first level verification
                            if(check.equals("takecare-"))
                            {
                                btnAction.setEnabled(true);
                                txtBarcodeValue.setText("QR Found!");
                                patID = Integer.parseInt(intentData.substring(9));
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
