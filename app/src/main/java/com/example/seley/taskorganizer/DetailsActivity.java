package com.example.seley.taskorganizer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Task> mTasks;
    public static String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        UUID taskID = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        mViewPager = findViewById(R.id.task_pager);
        mTasks = TaskStorage.get(getApplicationContext()).getTasks();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Task task = mTasks.get(position);
                return DetailsFragment.newInstance(task.getId());
            }

            @Override
            public int getCount() {
                return mTasks.size();
            }
        });

        for (int i = 0; i < mTasks.size(); i++)
        {
            if (mTasks.get(i).getId().equals(taskID))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID taskID)
    {
        Intent intent = new Intent(packageContext, DetailsActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskID);
        return intent;
    }

}
