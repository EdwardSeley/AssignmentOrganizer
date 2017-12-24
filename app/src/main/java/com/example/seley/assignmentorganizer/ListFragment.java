package com.example.seley.assignmentorganizer;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class ListFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, parent, false);
        mAssignmentRecyclerView = view.findViewById(R.id.assignment_recycler_view);
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
        AssignmentList list = AssignmentList.get(getActivity());
        List<Assignment> assignments = list.getAssignments();
        if (mAdapter == null)
        {
            mAdapter = new AssignmentAdapter(assignments);
            mAssignmentRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();
    }

    private class AssignmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View currentItem;
        private TextView mTitleView;
        private TextView mDateView;
        private Assignment mAssignment;
        private ImageView mCompletedCheckView;

        public AssignmentHolder(View v) {
            super(v);
            currentItem = v;
            currentItem.setOnClickListener(this);
            mTitleView = currentItem.findViewById(R.id.item_title);
            mDateView = currentItem.findViewById(R.id.item_date);
            mCompletedCheckView = currentItem.findViewById(R.id.completed_check);
        }

        public void bind(Assignment assignment)
        {
            mAssignment = assignment;
            mTitleView.setText(mAssignment.getTitle());
            mDateView.setText(mAssignment.getDueDate().toString());
            mCompletedCheckView.setVisibility(mAssignment.isCompleted() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = AssignmentPagerActivity.newIntent(getActivity(), mAssignment.getId());
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

        @Override
        public int getItemCount() {
            return mAssignments.size();
        }
    }
}
