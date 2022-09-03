package com.example.takecare_11.ui.patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.GetAllIDs;
import com.example.takecare_11.storageclasses.patient.Prescription;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDashboardPrescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDashboardPrescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientDashboardPrescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientDashboardPrescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDashboardPrescriptionFragment newInstance(String param1, String param2) {
        PatientDashboardPrescriptionFragment fragment = new PatientDashboardPrescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String id;
    private LinearLayout presList;
    private String[] presJson = null;
    Integer[] presIDs = null;



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
        id = bundle.getString("user_id");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_dashboard_prescription, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        presList = getView().findViewById(R.id.patient_dashboard_presription_list);
        presIDs = null;

        //get
        try {
            presIDs = new GetAllIDs(getContext(), "patient_"+id+"_prescriptions").execute().get().clone();
        }catch (Exception e)
        {
            return;
        }


        try{
            presJson = new String[presIDs[0]+1];  //give size
            presJson[0]=null;
            for(int i=1; i<=presIDs[0]; i++)
            {
                presJson[i] = new FieldGetter(getContext(),
                        Integer.toString(presIDs[i]),
                        "json_data",
                        "patient_"+id+"_prescriptions").execute().get();
            }

        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        //add items to list
        for(int i=1; i<=presIDs[0]; i++)
        {
            final int finalI = i;
            Prescription prescription = new Gson().fromJson(presJson[i], Prescription.class);

            View listItem = getLayoutInflater().inflate(R.layout.list_item_prescription, null, false);
            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    Intent intent = new Intent(getContext(), PatientViewPrescriptionActivity.class);
                    intent.putExtra("prescription_id", Integer.toString(presIDs[finalI]));
                    intent.putExtra("prescription", presJson[finalI]);
                    intent.putExtra("user_id", id);
                    startActivity(intent);

                }
            });

            listItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        v.setBackgroundColor(Color.parseColor("#40606060"));
                    else if(event.getAction()== MotionEvent.ACTION_UP)
                    {
                        Intent intent = new Intent(getContext(), PatientViewPrescriptionActivity.class);
                        intent.putExtra("prescription_id", Integer.toString(presIDs[finalI]));
                        intent.putExtra("prescription", presJson[finalI]);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        //set color back to normal
                        v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    }
                    else v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    return true;
                }
            });

            TextView titletv = listItem.findViewById(R.id.list_item_prescription_title);
            TextView doctv = listItem.findViewById(R.id.list_item_prescription_doc);
            titletv.setText(prescription.getTitle() );
            doctv.setText(prescription.getDocName());
            presList.addView(listItem);
        }


    }
}
