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

    public Note fetchNote(long id) {
        Note n = null;
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor c = db.query(true, NotesTable.TABLE_NOTES, NotesTable.availableColumns.toArray(new String[] {}), NotesTable.COLUMN_ID + " = " + id, null, null, null, null, null)) {
            if(c.moveToNext()) {
                String title = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_TITLE));
                String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                String body = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_BODY));
                n = new Note(title, body, new Category(category));
            }
        }
        return n;
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
