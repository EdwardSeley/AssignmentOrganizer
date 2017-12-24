package com.example.seley.assignmentorganizer;

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

public class AssignmentPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Assignment> mAssignments;
    public static String EXTRA_ASSIGNMENT_ID = "ASSIGNMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_pager);
        UUID assignmentID = (UUID) getIntent().getSerializableExtra(EXTRA_ASSIGNMENT_ID);
        mViewPager = findViewById(R.id.assignment_pager);
        mAssignments = AssignmentList.get(getApplicationContext()).getAssignments();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Assignment assignment = mAssignments.get(position);
                return DetailsFragment.newInstance(assignment.getId());
            }

            @Override
            public int getCount() {
                return mAssignments.size();
            }
        });

        for (int i = 0; i < mAssignments.size(); i++)
        {
            if (mAssignments.get(i).getId().equals(assignmentID))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID assignmentID)
    {
        Intent intent = new Intent(packageContext, AssignmentPagerActivity.class);
        intent.putExtra(EXTRA_ASSIGNMENT_ID, assignmentID);
        return intent;
    }

}
