package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import database.TaskDbSchema.TaskTable;

public class TaskBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "taskBase.db";

    public TaskBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskTable.NAME +
                "(" + " _id integer primary key autoincrement, " +
                TaskTable.Cols.UUID + ", " +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.SUBJECT + ", " +
                TaskTable.Cols.DATE + ", " +
                TaskTable.Cols.COMPLETED + ")" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
