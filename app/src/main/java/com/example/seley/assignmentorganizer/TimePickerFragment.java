package com.example.seley.assignmentorganizer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private TimePicker mTimePicker;
    public static String TAG = "com.example.seley.assignmentorganizer.date";
    public static String chosenTimeTag = "com.example.seley.assignmentorganizer.time";

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(TAG, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(TAG);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = view.findViewById(R.id.time_picker);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
                .setTitle("Time When Assignment is Due:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hours = mTimePicker.getHour();
                        int minutes = mTimePicker.getMinute();
                        calendar.set(Calendar.HOUR_OF_DAY, hours);
                        calendar.set(Calendar.MINUTE, minutes);
                        Date date = calendar.getTime();
                        sendResult(date);
                    }
                })
                .setView(view)
                .create();

    }

    public void sendResult(Date date)
    {
        Intent intent = new Intent();
        intent.putExtra(chosenTimeTag, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }


}
