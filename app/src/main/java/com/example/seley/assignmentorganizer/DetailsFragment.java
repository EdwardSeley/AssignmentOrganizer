package com.example.seley.assignmentorganizer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class DetailsFragment extends Fragment {
    private Assignment mAssignment;
    private EditText mTitleField;
    private CheckBox mCompletedCheckbox;
    private Button mDueDateButton;

    private static final String ARG_ASSIGNMENT_ID = "assignment_id";

    public static DetailsFragment newInstance(UUID assignmentID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSIGNMENT_ID, assignmentID);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID assignmentID = (UUID) getArguments().getSerializable(ARG_ASSIGNMENT_ID);
        mAssignment = AssignmentList.get(getActivity()).getAssignment(assignmentID);

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

        mDueDateButton = v.findViewById(R.id.due_date);
        mDueDateButton.setText(mAssignment.getDueDate().toString());
        mDueDateButton.setEnabled(false);

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
}
