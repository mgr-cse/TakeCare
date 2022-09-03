package com.example.takecare_11.ui.patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.GetAllIDs;
import com.example.takecare_11.storageclasses.pathologist.Pathologist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDashboardReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDashboardReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientDashboardReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientDashboardAlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDashboardReportFragment newInstance(String param1, String param2) {
        PatientDashboardReportFragment fragment = new PatientDashboardReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String id;
    private LinearLayout reportList;
    private  String[] pathoIDs = null;
    Integer[] reportIDs = null;

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
        return inflater.inflate(R.layout.fragment_patient_dashboard_reports, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        reportList = getView().findViewById(R.id.patient_dashboard_report_list);

        //get table ids
        try{
            reportIDs = new GetAllIDs(getContext(), "patient_" + id + "_reports").execute().get();
        }catch (Exception e)
        {
            return;
        }

        try{
            pathoIDs = new String[reportIDs[0] + 1];
            pathoIDs[0] = null;
            for(int i=0; i<=reportIDs[0]; i++){
                pathoIDs[i] = new FieldGetter(getContext(),
                        String.valueOf(reportIDs[i]),
                        "patho_id",
                        "patient_"+ id + "_reports").execute().get();
            }
        }catch (Exception e)
        {
            return;
        }

        //add items to list
        for(int i=1; i<=reportIDs[0]; i++)
        {
            final int finalI = i;
            Pathologist pathologist = new Pathologist(pathoIDs[i], getContext());

            final View listItem = getLayoutInflater().inflate(R.layout.list_item_prescription, null, false);

            listItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        v.setBackgroundColor(Color.parseColor("#40606060"));
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent intent = new Intent(getContext(), PatientViewReportActivity.class);
                        intent.putExtra("patho_id", pathoIDs[finalI]);
                        intent.putExtra("user_id", id);
                        intent.putExtra("report_id", String.valueOf(reportIDs[finalI]));
                        startActivity(intent);
                        //set color back to normal
                        v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    } else v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    return true;
                }
            });

            TextView pathoNametv = listItem.findViewById(R.id.list_item_prescription_title);
            TextView pathoIDtv = listItem.findViewById(R.id.list_item_prescription_doc);
            pathoNametv.setText(pathologist.getFullName());
            pathoIDtv.setText("Report");
            reportList.addView(listItem);
        }
    }
}
