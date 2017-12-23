package com.example.seley.assignmentorganizer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AssignmentListFragment extends Fragment {

    private RecyclerView mAssignmentRecyclerView;
    private AssignmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        mAssignmentRecyclerView = view.findViewById(R.id.assignment_recycler_view);
        mAssignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI()
    {
        AssignmentSingleton singleton = AssignmentSingleton.get(getActivity());
        List<Assignment> assignments = singleton.getAssignments();
        mAdapter = new AssignmentAdapter(assignments);
        mAssignmentRecyclerView.setAdapter(mAdapter);
    }

    private class AssignmentHolder extends RecyclerView.ViewHolder {

        public AssignmentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_assignment, parent, false));
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
            return new AssignmentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AssignmentHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mAssignments.size();
        }
    }
}
