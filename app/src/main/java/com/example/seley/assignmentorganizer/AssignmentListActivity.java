package com.example.seley.assignmentorganizer;

import android.support.v4.app.Fragment;

public class AssignmentListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment()
    {
        return new AssignmentListFragment();
    }
}
