package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.seley.taskorganizer.Task;

import java.util.Date;
import java.util.UUID;

import database.TaskDbSchema.TaskTable;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask()
    {
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        String subject = getString(getColumnIndex(TaskTable.Cols.SUBJECT));
        long time = getLong(getColumnIndex(TaskTable.Cols.DATE));
        int completed = getInt(getColumnIndex(TaskTable.Cols.COMPLETED));

        Task task = new Task(UUID.fromString(uuidString));
        task.setTitle(title);
        task.setSubject(subject);
        task.setDueDate(new Date(time));
        task.setCompleted(completed != 0);

        return task;
    }
}
