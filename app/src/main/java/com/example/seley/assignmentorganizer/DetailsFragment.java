package com.example.seley.assignmentorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private Assignment mAssignment;
    private EditText mTitleField;
    private EditText mSubjectField;
    private CheckBox mCompletedCheckbox;
    private Button mDueDateButton;

    private static final String ARG_ASSIGNMENT_ID = "assignment_id";
    private static final String DATE_PICKER_TAG = "DialogDate";
    private static final String TIME_PICKER_TAG = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public static DetailsFragment newInstance(UUID assignmentID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSIGNMENT_ID, assignmentID);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_assignment_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_assignment:
                AssignmentStorage.get(getActivity()).removeAssignment(mAssignment);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID assignmentID = (UUID) getArguments().getSerializable(ARG_ASSIGNMENT_ID);
        mAssignment = AssignmentStorage.get(getActivity()).getAssignment(assignmentID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_fragment, container, false);
        mTitleField = v.findViewById(R.id.assignment_title);
        mTitleField.setText(mAssignment.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAssignment.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSubjectField = v.findViewById(R.id.assignment_subject);
        mSubjectField.setText(mAssignment.getSubject());
        mSubjectField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAssignment.setSubject(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDueDateButton = v.findViewById(R.id.due_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy @ h:mm a", Locale.ENGLISH);
        mDueDateButton.setText(simpleDateFormat.format(mAssignment.getDueDate()));
        mDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment dialogFragment = DatePickerFragment
                        .newInstance(mAssignment.getDueDate());
                dialogFragment.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
                dialogFragment.show(fragmentManager, DATE_PICKER_TAG);
            }
        });

        mCompletedCheckbox = v.findViewById(R.id.completed);
        mCompletedCheckbox.setChecked(mAssignment.isCompleted());
        mCompletedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAssignment.setCompleted(isChecked);
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
            mAssignment.setDueDate(date);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AssignmentStorage.get(getActivity()).updateAssignment(mAssignment);
    }
}
