package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import database.AssignmentDbSchema.AssignmentTable;

public class AssignmentBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "assignmentBase.db";

    public AssignmentBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AssignmentTable.NAME +
                "(" + " _id integer primary key autoincrement, " +
                AssignmentTable.Cols.UUID + ", " +
                AssignmentTable.Cols.TITLE + ", " +
                AssignmentTable.Cols.SUBJECT + ", " +
                AssignmentTable.Cols.DATE + ", " +
                AssignmentTable.Cols.COMPLETED + ")" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
