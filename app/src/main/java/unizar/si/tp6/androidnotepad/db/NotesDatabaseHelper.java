package unizar.si.tp6.androidnotepad.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

import unizar.si.tp6.androidnotepad.note.Category;
import unizar.si.tp6.androidnotepad.note.Note;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        NotesTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        NotesTable.onUpgrade(db, oldVersion, newVersion);
    }

    public List<Category> getCategories() {
        SQLiteDatabase db = getReadableDatabase();
        List<Category> categories = new ArrayList<>();
        try(Cursor c = db.query(true, NotesTable.TABLE_NOTES, new String[] { NotesTable.COLUMN_CATEGORY }, null, null, null, null, null, null)) {
            while(c.moveToNext()) {
                String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                categories.add(new Category(category));
            }
        }
        return categories;
    }
}
