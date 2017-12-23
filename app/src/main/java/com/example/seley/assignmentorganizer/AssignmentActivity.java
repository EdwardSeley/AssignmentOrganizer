package com.example.seley.assignmentorganizer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AssignmentActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment()
    {
        return new AssignmentFragment();
    }
}
