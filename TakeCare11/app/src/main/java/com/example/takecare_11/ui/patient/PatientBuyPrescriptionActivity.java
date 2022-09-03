package com.example.takecare_11.ui.patient;

import android.Manifest;
import android.content.pm.PackageManager;
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

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.FieldSetter;
import com.example.takecare_11.storageclasses.patient.Prescription;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.IOException;

public class PatientBuyPrescriptionActivity extends AppCompatActivity {

    private String prescriptionID;
    private String userID;

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_buy_prescription);

        //get id
        prescriptionID = getIntent().getStringExtra("prescription_id");
        userID = getIntent().getStringExtra("user_id");

        initViews();

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.patient_buy_pres_barcode_state);
        surfaceView = findViewById(R.id.patient_buy_pres_surfaceView);
        btnAction = findViewById(R.id.patient_buy_pres_pay);
        btnAction.setEnabled(false);

        //set listener
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    //implement payment method
                    String result = new FieldSetter(getApplicationContext(), prescriptionID, "buy", "1", "patient_" + userID + "_prescriptions").execute().get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    PatientBuyPrescriptionActivity.this.finish();

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
                .setRequestedPreviewSize(800, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(PatientBuyPrescriptionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(PatientBuyPrescriptionActivity.this, new
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
