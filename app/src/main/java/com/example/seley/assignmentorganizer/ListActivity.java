package com.example.seley.assignmentorganizer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ListActivity extends AbstractFragment {

    @Override
    protected Fragment createFragment()
    {
        return new ListFragment();
    }
}
