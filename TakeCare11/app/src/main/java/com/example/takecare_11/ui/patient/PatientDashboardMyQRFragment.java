package com.example.takecare_11.ui.patient;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDashboardMyQRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDashboardMyQRFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientDashboardMyQRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientDashboardAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDashboardMyQRFragment newInstance(String param1, String param2) {
        PatientDashboardMyQRFragment fragment = new PatientDashboardMyQRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        id = bundle.getString("user_id");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_dashboard_my_qr, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ImageView iv = getView().findViewById(R.id.patient_my_qr_img);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            String patID = String.format("takecare-%010d", Integer.parseInt(id));

            BitMatrix bitMatrix = multiFormatWriter.encode(patID, BarcodeFormat.QR_CODE, 768, 768);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);

        }catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

