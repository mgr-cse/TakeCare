package com.example.takecare_11.storageclasses.patient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.takecare_11.R;
import com.example.takecare_11.ui.AlertReceiver;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Prescription implements Serializable {
    String doc, title, notes;

    class Dose implements Serializable
    {
        String med;
        String qtyType;
        int qty;
        int days;
        boolean mor, aft, eve;

        int tCnt;   //time count
        int[] hrs, min;
    }
    int dosCnt;
    Dose[] doses;

    //methods
    public Prescription(String doc, String title, String notes)
    {
        this.doc = doc;
        this.title = title;
        this.notes = notes;
        dosCnt = 0;
        doses = null;
    }

    public void setMaxDoseCount(int count)
    {
        doses = new Dose[count];
    }

    public void addDose(String med,
                        String qtyType,
                        int qty,
                        int days,
                        boolean mor, boolean aft, boolean eve,
                        int tCnt,
                        int[] hrs, int[] min)
    {
        doses[dosCnt] = new Dose();

        doses[dosCnt].med = med;
        doses[dosCnt].qtyType = qtyType;
        doses[dosCnt].qty = qty;
        doses[dosCnt].days = days;
        doses[dosCnt].mor = mor; doses[dosCnt].aft = aft; doses[dosCnt].eve = eve;
        try {

            doses[dosCnt].tCnt = tCnt;
            doses[dosCnt].hrs = hrs.clone();
            doses[dosCnt].min = min.clone();
        }catch (Exception e)
        {
            doses[dosCnt].tCnt = 0;
            doses[dosCnt].hrs = null;
            doses[dosCnt].min = null;
        }

        dosCnt++;
    }

    //getters
    public String getDocName()
    {
        return doc;
    }
    public String getTitle()
    {
        return title;
    }
    public String getNotes()
    {
        return notes;
    }

    //add doses to view
    public void addDosesToLinearLayout(LayoutInflater inflater, LinearLayout ll)
    {
        for(int i=0; i<dosCnt; i++) {
            View v = inflater.inflate(R.layout.list_item_dose, null, false);

            TextView titleTv = v.findViewById(R.id.list_item_dose_title);
            TextView qtyTv = v.findViewById(R.id.list_item_dose_qty);
            TextView timeTv = v.findViewById(R.id.list_item_dose_times);

            titleTv.setText(doses[i].med + " for "+ doses[i].days+ " days");

            //set quick times
            String s = "Quantity: "+doses[i].qty + " " + doses[i].qtyType + " ";
            if(doses[i].mor)
                s = s + "Morning, ";
            if(doses[i].aft)
                s = s + "Afternoon, ";
            if(doses[i].eve)
                s = s + "Evening,";
            qtyTv.setText(s);

            //set times
            s = "";
            for(int j=0; j<doses[i].tCnt; j++)
                s = s + String.format("%02d:%02d\n", doses[i].hrs[j], doses[i].min[j]);
            timeTv.setText(s);

            //commit
            ll.addView(v);
        }

    }

    //alarms
    private void MakeAlarm(int hrs, int min, int days, Context context, String medName,
                           int requestCode)
    {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hrs);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, days);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("alert_title", medName);
        intent.putExtra("alert_message", "Take your medicine");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void CancelAlarm(Context context, String medName, int requestCode)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("alert_title", medName);
        intent.putExtra("alert_message", "Take your medicine Prescription: " + title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    private int GetRequestCode(int id, int e_dosCnt, int alarmCnt)
    {
        String code = String.format("%d%01d%02d", id, e_dosCnt, alarmCnt);
        return Integer.parseInt(code);
    }

    //set alarms
    public void SetAlarms(Context context, int id)
    {
        for(int i=0; i<dosCnt; i++)
        {
            int alarmId = 0;
           for(int j=0; j<doses[i].tCnt; j++)
           {
               int hrs = doses[i].hrs[j];
               int min = doses[i].min[j];
               for(int k=0; k<doses[i].days; k++)
               {
                   int requestCode = GetRequestCode(id, i, alarmId++);
                   MakeAlarm(hrs, min, k, context, doses[i].med, requestCode);
               }
           }

           for(int k=0; k<doses[i].days; k++)
           {
               if(doses[i].mor)
               {
                   int requestCode = GetRequestCode(id, i, alarmId++);
                   MakeAlarm(9, 0, k, context, doses[i].med, requestCode);
               }
               if(doses[i].aft)
               {
                   int requestCode = GetRequestCode(id, i, alarmId++);
                   MakeAlarm(14, 0, k, context, doses[i].med, requestCode);
               }
               if(doses[i].eve)
               {
                   int requestCode = GetRequestCode(id, i, alarmId++);
                   MakeAlarm(20, 0, k, context, doses[i].med, requestCode);
               }

           }

        }
    }

    public void CancelAllAlarms(Context context, int id)
    {
        for(int i=0; i<dosCnt; i++)
        {
            int alarmId = 0;
            for(int j=0; j<doses[i].tCnt; j++)
            {
                int hrs = doses[i].hrs[j];
                int min = doses[i].min[j];
                for(int k=0; k<doses[i].days; k++)
                {
                    int requestCode = GetRequestCode(id, i, alarmId++);
                    CancelAlarm(context, doses[i].med, requestCode);
                }
            }

            for(int k=0; k<doses[i].days; k++)
            {
                if(doses[i].mor)
                {
                    int requestCode = GetRequestCode(id, i, alarmId++);
                    CancelAlarm(context, doses[i].med, requestCode);
                }
                if(doses[i].aft)
                {
                    int requestCode = GetRequestCode(id, i, alarmId++);
                    CancelAlarm(context, doses[i].med, requestCode);
                }
                if(doses[i].eve)
                {
                    int requestCode = GetRequestCode(id, i, alarmId++);
                    CancelAlarm(context, doses[i].med, requestCode);
                }

            }

        }
    }
}
