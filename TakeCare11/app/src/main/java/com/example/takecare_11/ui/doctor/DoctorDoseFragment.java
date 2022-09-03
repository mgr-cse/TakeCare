package com.example.takecare_11.ui.doctor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.takecare_11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorDoseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorDoseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorDoseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorDoseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorDoseFragment newInstance(String param1, String param2) {
        DoctorDoseFragment fragment = new DoctorDoseFragment();
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

    Spinner qtyType;
    Button addTime;
    TableLayout times;
    TextView timeView;
    int timeCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_dose, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //init
        //time count
        timeCount = 0;
        timeView = getView().findViewById(R.id.doctor_dose_time_count);
        timeView.setText(Integer.toString(timeCount));

        //qtyType spinner
        qtyType = getView().findViewById(R.id.doctor_dose_qty_type_spinner);
        String qtyTypes[] = new String[]
                {
                        "Tab", "ml", "mg"
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                qtyTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtyType.setAdapter(adapter);

        //times table
        times = getView().findViewById(R.id.doctor_dose_time_table);

        //add time button
        addTime = getView().findViewById(R.id.doctor_dose_add_time);

        //set onClick listener
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //programatically generate layout

                TableRow tr = new TableRow(getContext());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

                EditText hrs = new EditText(getContext());
                hrs.setTag("doseHrs"+timeCount);
                EditText min = new EditText(getContext());
                min.setTag("doseMin"+timeCount);
                TextView tv = new TextView(getContext());
                tv.setText(":");

                hrs.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                hrs.setHint("HH");
                min.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                min.setHint("MM");

                tr.addView(hrs);
                tr.addView(tv);
                tr.addView(min);
                times.addView(tr);

                timeCount++;
                timeView.setText(Integer.toString(timeCount));

            }
        });


    }
}
