package com.example.zito.ittcheckbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Zito on 11/4/2015.
 * Capstone Project - ITT-Tech Westminster CO * Sep - Dec - 2105
 * ==== Julio C. =====
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView trDate, jrDate;
    TextView acctDate;

    public DateDialog(View view) {

        trDate = (TextView) view;
        acctDate = (TextView) view;
        jrDate = (TextView) view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = ((month + 1) + "/" + day + "/" + year);
        acctDate.setText(date);
        trDate.setText(date);
        jrDate.setText(date);

    }
}
