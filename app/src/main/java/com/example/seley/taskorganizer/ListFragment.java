package com.example.seley.taskorganizer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private Button mNoTasksButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.get(getActivity()).addTask(task);
                Intent intent = DetailsActivity.newIntent(getActivity(), task.getId());
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
        mTaskRecyclerView = view.findViewById(R.id.task_recycler_view);
        mNoTasksButton = view.findViewById(R.id.add_only_task);
        mNoTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task();
                TaskStorage.get(getActivity()).addTask(task);
                Intent intent = DetailsActivity.newIntent(getActivity(), task.getId());
                startActivity(intent);
            }
        });
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
        TaskStorage list = TaskStorage.get(getActivity());
        List<Task> tasks = list.getTasks();

        if (tasks.isEmpty())
            mTaskRecyclerView.setVisibility(View.GONE);

        if (mAdapter == null)
        {
            if (mTaskRecyclerView != null)
            {
                mAdapter = new TaskAdapter(tasks);
                mTaskRecyclerView.setAdapter(mAdapter);
            }
        }
        else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View currentItem;
        private TextView mTitleView;
        private TextView mDateView;
        private TextView mSubjectView;
        private Task mTask;
        private ImageView mCompletedCheckView;

        public TaskHolder(View v) {
            super(v);
            currentItem = v;
            currentItem.setOnClickListener(this);
            mTitleView = currentItem.findViewById(R.id.item_title);
            mSubjectView = currentItem.findViewById(R.id.item_subject);
            mDateView = currentItem.findViewById(R.id.item_date);
            mCompletedCheckView = currentItem.findViewById(R.id.completed_check);
        }

        public void bind(Task task)
        {
            mTask = task;

            String title = mTask.getTitle();
            mTitleView.setText(title.isEmpty() ? "No Title" : title);

            String subject = mTask.getSubject();
            mSubjectView.setText(subject.isEmpty() ? "No Subject" : subject);

            Date date = mTask.getDueDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy @ h:mm a", Locale.ENGLISH);
            mDateView.setText(simpleDateFormat.format(date));
            mCompletedCheckView.setVisibility(mTask.isCompleted() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailsActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
        }

    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        TaskAdapter(List<Task> tasks)
        {
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        public void setTasks(List<Task> mTasks) {
            this.mTasks = mTasks;
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }
}
