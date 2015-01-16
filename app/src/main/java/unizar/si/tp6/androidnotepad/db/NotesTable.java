package unizar.si.tp6.androidnotepad.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NotesTable {
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_CATEGORY = "category";
    public static final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_BODY, COLUMN_CATEGORY }));

    private static final String DATABASE_CREATE =
            "create table " + TABLE_NOTES + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_TITLE + " text not null, "
                    + COLUMN_BODY + " text not null, "
                    + COLUMN_CATEGORY + " text not null);";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL("insert into notes(title, body, category) values ('tit0', 'bod0', 'cat0');");
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(NotesTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(db);
    }
}
