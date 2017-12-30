package com.example.seley.assignmentorganizer;

import android.support.v4.app.Fragment;

public class ListActivity extends AbstractActivity {

    @Override
    protected Fragment createFragment()
    {
        return new ListFragment();
    }
}
