package com.example.seley.assignmentorganizer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.AssignmentBaseHelper;
import database.AssignmentCursorWrapper;
import database.AssignmentDbSchema;
import database.AssignmentDbSchema.AssignmentTable;

public class AssignmentStorage {

    private static AssignmentStorage sAssignment;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static AssignmentStorage get(Context context)
    {
        if (sAssignment == null)
            sAssignment = new AssignmentStorage(context);
        return sAssignment;
    }

    private AssignmentStorage(Context context)
    {
        mContext = context;
        mDatabase = new AssignmentBaseHelper(context).getWritableDatabase();
    }

    private static ContentValues getContentValues (Assignment assignment)
    {
        ContentValues values = new ContentValues();
        values.put(AssignmentTable.Cols.UUID, assignment.getId().toString());
        values.put(AssignmentTable.Cols.TITLE, assignment.getTitle());
        values.put(AssignmentTable.Cols.SUBJECT, assignment.getSubject());
        values.put(AssignmentTable.Cols.DATE, assignment.getDueDate().getTime());
        values.put(AssignmentTable.Cols.COMPLETED, assignment.isCompleted() ? 1 : 0 );
        return values;
    }

    public void addAssignment(Assignment assignment) {
        ContentValues values = getContentValues(assignment);
        mDatabase.insert(AssignmentTable.NAME, null, values);
    }

    public void updateAssignment(Assignment assignment)
    {
        String uuidString = assignment.getId().toString();
        ContentValues values = getContentValues(assignment);
        mDatabase.update(AssignmentTable.NAME, values, AssignmentTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private AssignmentCursorWrapper query(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                AssignmentTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new AssignmentCursorWrapper(cursor);
    }

    public void removeAssignment(Assignment assignment) {
        mDatabase.delete(AssignmentTable.NAME, AssignmentTable.Cols.UUID + " = ?", new String[]{assignment.getId().toString()});
    }

    public List<Assignment> getAssignments()
    {
        List<Assignment> assignments = new ArrayList<>();

        AssignmentCursorWrapper cursor = query(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Assignment assignment = cursor.getAssignment();
                assignments.add(assignment);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return assignments;
    }

    public boolean checkWhetherEmpty()
    {
        AssignmentCursorWrapper cursor = query(null, null);

        try {
            if (cursor.getCount() == 0)
                return true;
            else return false;
        }
        finally {
            cursor.close();
        }
    }

    public Assignment getAssignment(UUID id)
    {
        AssignmentCursorWrapper cursor = query(AssignmentTable.Cols.UUID + " = ?", new String[] { id.toString()});
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToFirst();
            return cursor.getAssignment();
        }
        finally {
            cursor.close();
        }
    }
}
