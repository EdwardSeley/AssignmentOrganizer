package com.example.seley.taskorganizer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.TaskBaseHelper;
import database.TaskCursorWrapper;
import database.TaskDbSchema.TaskTable;

public class TaskStorage {

    private static TaskStorage sTaskStorage;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private TaskNotificationManager notificationManager;

    public static TaskStorage get(Context context)
    {
        if (sTaskStorage == null)
            sTaskStorage = new TaskStorage(context);
        return sTaskStorage;
    }

    private TaskStorage(Context context)
    {
        mContext = context;
        mDatabase = new TaskBaseHelper(context).getWritableDatabase();
        notificationManager = new TaskNotificationManager(context);
    }

    private static ContentValues getContentValues (Task task)
    {
        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.SUBJECT, task.getSubject());
        values.put(TaskTable.Cols.DATE, task.getDueDate().getTime());
        values.put(TaskTable.Cols.COMPLETED, task.isCompleted() ? 1 : 0 );
        return values;
    }

    public void addTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskTable.NAME, null, values);
        notificationManager.addNotification(task);
    }

    public void updateTask(Task task)
    {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);
        mDatabase.update(TaskTable.NAME, values, TaskTable.Cols.UUID + " = ?", new String[] {uuidString});
        notificationManager.updateNotification(task);
    }

    private TaskCursorWrapper query(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                TaskTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TaskCursorWrapper(cursor);
    }

    public void removeTask(Task task) {
        mDatabase.delete(TaskTable.NAME, TaskTable.Cols.UUID + " = ?", new String[]{task.getId().toString()});
        notificationManager.removeNotification(task);
    }

    public List<Task> getTasks()
    {
        List<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = query(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Task task = cursor.getTask();
                tasks.add(task);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return tasks;
    }

    public Task getTask(UUID id)
    {
        TaskCursorWrapper cursor = query(TaskTable.Cols.UUID + " = ?", new String[] { id.toString()});
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToFirst();
            return cursor.getTask();
        }
        finally {
            cursor.close();
        }
    }
}
