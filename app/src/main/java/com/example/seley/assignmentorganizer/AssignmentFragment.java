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

public class AssignmentFragment extends Fragment {
    private Assignment mAssignment;
    private EditText mTitleField;
    private CheckBox mCompletedCheckbox;
    private Button mDueDateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAssignment = new Assignment();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment, container, false);

        mTitleField = v.findViewById(R.id.assignment_title);
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
        mCompletedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAssignment.setCompleted(isChecked);
            }
        });

        return v;
    }
}
