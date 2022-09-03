package com.example.takecare_11.ui.pathologist;

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
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.database.api.FieldGetter;
import com.example.takecare_11.storageclasses.database.api.GetAllIDs;
import com.example.takecare_11.storageclasses.patient.Patient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PathologistQueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PathologistQueueFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PathologistQueueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PathologistQueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PathologistQueueFragment newInstance(String param1, String param2) {
        PathologistQueueFragment fragment = new PathologistQueueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private String id;
    private LinearLayout queueList;
    private String[] patIDs = null;
    Integer[] queueIDs = null;

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
        return inflater.inflate(R.layout.fragment_pathologist_queue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        queueList = getView().findViewById(R.id.patho_dashboard_queue_list);

        //get table ids
        try {
            queueIDs = new GetAllIDs(getContext(), "patho_" + id + "_queue").execute().get().clone();
        } catch (Exception e) {
            //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            patIDs = new String[queueIDs[0] + 1];
            patIDs[0] = null;
            for (int i = 1; i <= queueIDs[0]; i++) {
                patIDs[i] = new FieldGetter(getContext(),
                        String.valueOf(queueIDs[i]),
                        "patient_id",
                        "patho_" + id + "_queue").execute().get();
            }
        } catch (Exception e) {
            //Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        //add items to list
        for (int i = 1; i <= queueIDs[0]; i++) {
            final int finalI = i;
            Patient patient = new Patient(patIDs[i], getContext());

            View listItem = getLayoutInflater().inflate(R.layout.list_item_prescription, null, false);

            listItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        v.setBackgroundColor(Color.parseColor("#40606060"));
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent intent = new Intent(getContext(), PathologistGiveReportActivity.class);
                        intent.putExtra("patient_id", patIDs[finalI]);
                        intent.putExtra("user_id", id);
                        intent.putExtra("queue_id", String.valueOf(queueIDs[finalI]));
                        startActivity(intent);
                        //set color back to normal
                        v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    } else v.setBackgroundColor(Color.parseColor("#F9F9F9"));
                    return true;
                }
            });

            TextView patNametv = listItem.findViewById(R.id.list_item_prescription_title);
            TextView patIDtv = listItem.findViewById(R.id.list_item_prescription_doc);
            patNametv.setText(patient.getFullName());
            patIDtv.setText("id: " + patIDs[i]);
            queueList.addView(listItem);

        }
    }
}
