package com.example.takecare_11.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.takecare_11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorDashboardMakePrescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorDashboardMakePrescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorDashboardMakePrescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorDashboardMakePrescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorDashboardMakePrescriptionFragment newInstance(String param1, String param2) {
        DoctorDashboardMakePrescriptionFragment fragment = new DoctorDashboardMakePrescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String id;
    private Button createPrescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        //init id
        id = bundle.getString("user_id");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_dashboard_make_prescription, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init
        createPrescription = getView().findViewById(R.id.doctor_dashboard_make_prescription_btn);

        //add listener
        createPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoctorMakePrescriptionActivity.class);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

    }
}
