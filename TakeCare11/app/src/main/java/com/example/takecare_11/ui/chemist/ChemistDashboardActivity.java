package com.example.takecare_11.ui.chemist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.signin.SignInActivity;
import com.example.takecare_11.ui.UpdateAccountActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;

public class ChemistDashboardActivity extends AppCompatActivity {

    private String id;
    private Toolbar toolbar;
    private ImageView imageView;

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
                intent = new Intent(this, ChemistUpdateIDActivity.class);
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
        setContentView(R.layout.activity_chemist_dashboard);

        //init
        id = getIntent().getStringExtra("user_id");
        toolbar = findViewById(R.id.chem_dashboard_toolbar);
        imageView = findViewById(R.id.chem_dashboard_qr);

        //toolbar
        setSupportActionBar(toolbar);

        //qr
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            String chemID = String.format("takecare-%010d", Integer.parseInt(id));

            BitMatrix bitMatrix = multiFormatWriter.encode(chemID, BarcodeFormat.QR_CODE, 768, 768);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
