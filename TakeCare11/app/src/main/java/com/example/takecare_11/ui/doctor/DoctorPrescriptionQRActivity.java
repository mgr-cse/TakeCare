package com.example.takecare_11.ui.doctor;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class DoctorPrescriptionQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription_q_r);

        //get prescription
        String prescription = getIntent().getStringExtra("prescription");

        //generate qr
        ImageView iv = findViewById(R.id.doctor_prescription_q_r_image);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(prescription, BarcodeFormat.QR_CODE, 768, 768);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Failed to generate QR code",Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }
}
