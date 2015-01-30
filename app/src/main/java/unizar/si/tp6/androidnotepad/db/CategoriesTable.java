package unizar.si.tp6.androidnotepad.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by dbarelop on 30/01/15.
 */
public abstract class CategoriesTable {
    // TODO: integrar
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(new String[]{COLUMN_ID, COLUMN_NOTE_ID, COLUMN_CATEGORY}));

    public static final String DATABASE_CREATE =
            "create table " + TABLE_CATEGORIES + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_NOTE_ID + " integer not null, "
                    + COLUMN_CATEGORY + " text not null, "
                    + "foregin key (" + COLUMN_NOTE_ID + ") references " + NotesTable.TABLE_NOTES + "(" + NotesTable.COLUMN_ID + "));";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CategoriesTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_CATEGORIES);
        onCreate(db);
    }
}
