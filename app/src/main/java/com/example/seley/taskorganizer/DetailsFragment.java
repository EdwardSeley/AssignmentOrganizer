package com.example.seley.taskorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class DetailsFragment extends Fragment {
    private Task mTask;
    private EditText mTitleField;
    private EditText mSubjectField;
    private CheckBox mCompletedCheckbox;
    private Button mDueDateButton;

    private static final String ARG_TASK_ID = "task_id";
    private static final String DATE_PICKER_TAG = "DialogDate";
    private static final String TIME_PICKER_TAG = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public static DetailsFragment newInstance(UUID taskID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskID);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_task:
                TaskStorage.get(getActivity()).removeTask(mTask);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskID = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskStorage.get(getActivity()).getTask(taskID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_fragment, container, false);
        mTitleField = v.findViewById(R.id.task_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTask.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSubjectField = v.findViewById(R.id.task_subject);
        mSubjectField.setText(mTask.getSubject());
        mSubjectField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTask.setSubject(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDueDateButton = v.findViewById(R.id.due_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy @ h:mm a", Locale.ENGLISH);
        mDueDateButton.setText(simpleDateFormat.format(mTask.getDueDate()));
        mDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment = DatePickerFragment
                        .newInstance(mTask.getDueDate());
                dialogFragment.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
                dialogFragment.show(fragmentManager, DATE_PICKER_TAG);
            }
        });

        mCompletedCheckbox = v.findViewById(R.id.completed);
        mCompletedCheckbox.setChecked(mTask.isCompleted());
        mCompletedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mTask.setCompleted(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_DATE)
        {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.chosenDateTag);
            FragmentManager fragmentManager = getFragmentManager();
            DialogFragment dialogFragment = TimePickerFragment
                    .newInstance(date);
            dialogFragment.setTargetFragment(DetailsFragment.this, REQUEST_TIME);
            dialogFragment.show(fragmentManager, TIME_PICKER_TAG);

        }

        if (requestCode == REQUEST_TIME)
        {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.chosenTimeTag);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy @ h:mm a", Locale.ENGLISH);
            mDueDateButton.setText(simpleDateFormat.format(date));
            mTask.setDueDate(date);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskStorage.get(getActivity()).updateTask(mTask);
    }
}
