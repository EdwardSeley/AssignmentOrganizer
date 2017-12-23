package com.example.seley.assignmentorganizer;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssignmentSingleton {
    private static AssignmentSingleton sAssignment;

    private List<Assignment> mAssignments;

    public static AssignmentSingleton get(Context context)
    {
        if (sAssignment == null)
            sAssignment = new AssignmentSingleton(context);
        return sAssignment;
    }

    private AssignmentSingleton(Context context)
    {
        mAssignments = new ArrayList<>();
        for (int i = 1; i <= 100; i++)
        {
            Assignment assignment = new Assignment();
            assignment.setTitle("Assignment #" + i);
            assignment.setCompleted( (i % 2) == 0);
            mAssignments.add(assignment);

        }
    }

    public List<Assignment> getAssignments()
    {
        return mAssignments;
    }

    public Assignment getAssignment(UUID id)
    {
        for (Assignment assignment : mAssignments)
            if (assignment.getId().equals(id))
                return assignment;

        return null;
    }
}
