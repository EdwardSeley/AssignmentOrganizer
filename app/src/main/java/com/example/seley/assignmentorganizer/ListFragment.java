package com.example.seley.assignmentorganizer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mAdapter;
    private TextView mNoAssignmentsView;
    private Button mNoAssignmentsButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_assignment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.new_assignment:
                Assignment assignment = new Assignment();
                AssignmentStorage.get(getActivity()).addAssignment(assignment);
                Intent intent = DetailsActivity.newIntent(getActivity(), assignment.getId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, parent, false);
        mAssignmentRecyclerView = view.findViewById(R.id.assignment_recycler_view);
        mNoAssignmentsButton = view.findViewById(R.id.add_only_assignment);
        mNoAssignmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assignment assignment = new Assignment();
                AssignmentStorage.get(getActivity()).addAssignment(assignment);
                Intent intent = DetailsActivity.newIntent(getActivity(), assignment.getId());
                startActivity(intent);
            }
        });

        mNoAssignmentsView = view.findViewById(R.id.no_assignment_textView);
        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI()
    {
        AssignmentStorage list = AssignmentStorage.get(getActivity());
        List<Assignment> assignments = list.getAssignments();

        if (assignments.isEmpty())
            mAssignmentRecyclerView.setVisibility(View.GONE);

        if (mAdapter == null)
        {
            if (mAssignmentRecyclerView != null)
            {
                mAdapter = new AssignmentAdapter(assignments);
                mAssignmentRecyclerView.setAdapter(mAdapter);
            }
        }
        else {
            mAdapter.setAssignments(assignments);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class AssignmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View currentItem;
        private TextView mTitleView;
        private TextView mDateView;
        private TextView mSubjectView;
        private Assignment mAssignment;
        private ImageView mCompletedCheckView;

        public AssignmentHolder(View v) {
            super(v);
            currentItem = v;
            currentItem.setOnClickListener(this);
            mTitleView = currentItem.findViewById(R.id.item_title);
            mSubjectView = currentItem.findViewById(R.id.item_subject);
            mDateView = currentItem.findViewById(R.id.item_date);
            mCompletedCheckView = currentItem.findViewById(R.id.completed_check);
        }

        public void bind(Assignment assignment)
        {
            mAssignment = assignment;
            mTitleView.setText(mAssignment.getTitle());
            mSubjectView.setText(mAssignment.getSubject());
            Date date = mAssignment.getDueDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.ENGLISH);
            mDateView.setText(simpleDateFormat.format(date));
            mCompletedCheckView.setVisibility(mAssignment.isCompleted() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailsActivity.newIntent(getActivity(), mAssignment.getId());
            startActivity(intent);
        }

    }

    private class AssignmentAdapter extends RecyclerView.Adapter<AssignmentHolder> {

        private List<Assignment> mAssignments;

        AssignmentAdapter(List<Assignment> assignments)
        {
            mAssignments = assignments;
        }

        @Override
        public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AssignmentHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(AssignmentHolder holder, int position) {
            Assignment assignment = mAssignments.get(position);
            holder.bind(assignment);
        }

        public void setAssignments(List<Assignment> mAssignments) {
            this.mAssignments = mAssignments;
        }

        @Override
        public int getItemCount() {
            return mAssignments.size();
        }
    }
}
