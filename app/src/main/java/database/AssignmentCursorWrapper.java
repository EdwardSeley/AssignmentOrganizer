package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.seley.assignmentorganizer.Assignment;

import java.util.Date;
import java.util.UUID;

import database.AssignmentDbSchema.AssignmentTable;

public class AssignmentCursorWrapper extends CursorWrapper {

    public AssignmentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Assignment getAssignment ()
    {
        String uuidString = getString(getColumnIndex(AssignmentTable.Cols.UUID));
        String title = getString(getColumnIndex(AssignmentTable.Cols.TITLE));
        String subject = getString(getColumnIndex(AssignmentTable.Cols.SUBJECT));
        long time = getLong(getColumnIndex(AssignmentTable.Cols.DATE));
        int completed = getInt(getColumnIndex(AssignmentTable.Cols.COMPLETED));

        Assignment assignment = new Assignment(UUID.fromString(uuidString));
        assignment.setTitle(title);
        assignment.setSubject(subject);
        assignment.setDueDate(new Date(time));
        assignment.setCompleted(completed != 0);

        return assignment;
    }
}
