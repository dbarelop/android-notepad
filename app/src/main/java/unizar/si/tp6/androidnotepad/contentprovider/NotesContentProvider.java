package unizar.si.tp6.androidnotepad.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import unizar.si.tp6.androidnotepad.db.NotesDatabaseHelper;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.test.Testable;
import unizar.si.tp6.androidnotepad.test.Tests;
import unizar.si.tp6.androidnotepad.test.contentprovider.NotesContentProviderTests;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NotesContentProvider extends ContentProvider implements Testable {
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/notes";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/note";
    private static final int NOTES = 10;
    private static final int NOTE_ID = 20;
    private static final int CATEGORIES = 30;
    private static final int NOTES_CATEGORY = 40;
    private static final String AUTHORITY = "unizar.si.tp6.androidnotepad.contentprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/CATEGORIES", CATEGORIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/CATEGORY/*", NOTES_CATEGORY);
    }

    private NotesDatabaseHelper database;

    @Override
    public boolean onCreate() {
        database = new NotesDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(NotesTable.TABLE_NOTES);
        int uriType = sURIMatcher.match(uri);
        String groupBy = null;
        switch (uriType) {
            case NOTES:
                break;
            case NOTE_ID:
                queryBuilder.appendWhere(NotesTable.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            case CATEGORIES:
                groupBy = NotesTable.COLUMN_CATEGORY;
                if (TextUtils.isEmpty(selection)) {
                    selection = NotesTable.COLUMN_CATEGORY + " is not null";
                } else {
                    selection += " and " + NotesTable.COLUMN_CATEGORY + " is not null";
                }
                break;
            case NOTES_CATEGORY:
                queryBuilder.appendWhere(NotesTable.COLUMN_CATEGORY + " = '" + uri.getLastPathSegment().replace("'", "''") + "'");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Inserts a new row in the content provider.
     *
     * @param uri    The content:// URL of the insertion request. This must not be null.
     * @param values A set of column_name/value pairs to add to the database. This must not be null.
     * @return The URI for the newly inserted item.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        long id;
        switch (uriType) {
            case NOTES:
                id = db.insert(NotesTable.TABLE_NOTES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    /**
     * Deletes one or more rows in the content provider.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order they appear in the selection.
     *                      The values will be bound as Strings.
     * @return The number of rows affected.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case NOTES:
                rowsDeleted = db.delete(NotesTable.TABLE_NOTES, selection, selectionArgs);
                break;
            case NOTE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(NotesTable.TABLE_NOTES, NotesTable.COLUMN_ID + " = " + id, null);
                } else {
                    rowsDeleted = db.delete(NotesTable.TABLE_NOTES, NotesTable.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /**
     * Updates one or more rows in the content provider.
     * @param uri The URI to query. This can potentially have a record ID if this is an update request for a specific record.
     * @param values A set of column_name/value pairs to add to the database. This must not be null.
     * @param selection An optional filter to match rows to update.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order they appear in the selection.
     *                      The values will be bound as Strings.
     * @return The number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case NOTES:
                rowsUpdated = db.update(NotesTable.TABLE_NOTES, values, selection, selectionArgs);
                break;
            case NOTE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_ID + " = " + id, null);
                } else {
                    rowsUpdated = db.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            case NOTES_CATEGORY:
                String category = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_CATEGORY + " = ?", new String[]{category});
                } else {
                    String[] selectionArgs_ = selectionArgs == null ? new String[]{category} : new String[selectionArgs.length + 1];
                    if (selectionArgs != null) {
                        selectionArgs_[0] = category;
                        for (int i = 0; i < selectionArgs.length; i++) {
                            selectionArgs_[i + 1] = selectionArgs[i];
                        }
                    }
                    rowsUpdated = db.update(NotesTable.TABLE_NOTES, values, NotesTable.COLUMN_CATEGORY + " = ? and " + selection, selectionArgs_);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            if (!NotesTable.availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    /*------------------------------------------ TESTS -------------------------------------------*/

    @Override
    public Tests<NotesContentProvider> getTests() {
        return new NotesContentProviderTests();
    }

    /*private static final ContentValues[] insertTESTS_values = new ContentValues[4];
    private static final ContentValues[] updateTESTS_values = new ContentValues[4];

    static {
        for (int i = 0; i < Math.max(insertTESTS_values.length, updateTESTS_values.length); i++) {
            if (i < insertTESTS_values.length) insertTESTS_values[i] = new ContentValues();
            if (i < updateTESTS_values.length) updateTESTS_values[i] = new ContentValues();
        }
        insertTESTS_values[0].put(NotesTable.COLUMN_TITLE, "tit");
        insertTESTS_values[0].put(NotesTable.COLUMN_CATEGORY, "cat");
        insertTESTS_values[0].put(NotesTable.COLUMN_BODY, "bod");
        insertTESTS_values[1].put(NotesTable.COLUMN_TITLE, (String) null);
        insertTESTS_values[1].put(NotesTable.COLUMN_CATEGORY, "cat");
        insertTESTS_values[1].put(NotesTable.COLUMN_BODY, "bod");
        insertTESTS_values[2].put(NotesTable.COLUMN_TITLE, "");
        insertTESTS_values[2].put(NotesTable.COLUMN_CATEGORY, "cat");
        insertTESTS_values[2].put(NotesTable.COLUMN_BODY, "bod");
        insertTESTS_values[3].put(NotesTable.COLUMN_TITLE, "tit");
        insertTESTS_values[3].put(NotesTable.COLUMN_CATEGORY, "cat");
        insertTESTS_values[3].put(NotesTable.COLUMN_BODY, (String) null);
        updateTESTS_values[0].put(NotesTable.COLUMN_TITLE, "tit");
        updateTESTS_values[0].put(NotesTable.COLUMN_CATEGORY, "cat");
        updateTESTS_values[0].put(NotesTable.COLUMN_BODY, "bod");
        updateTESTS_values[1].put(NotesTable.COLUMN_TITLE, (String) null);
        updateTESTS_values[1].put(NotesTable.COLUMN_CATEGORY, "cat");
        updateTESTS_values[1].put(NotesTable.COLUMN_BODY, "bod");
        updateTESTS_values[2].put(NotesTable.COLUMN_TITLE, "");
        updateTESTS_values[2].put(NotesTable.COLUMN_CATEGORY, "cat");
        updateTESTS_values[2].put(NotesTable.COLUMN_BODY, "bod");
        updateTESTS_values[3].put(NotesTable.COLUMN_TITLE, "tit");
        updateTESTS_values[3].put(NotesTable.COLUMN_CATEGORY, "cat");
        updateTESTS_values[3].put(NotesTable.COLUMN_BODY, (String) null);
    }

    private static final Object[][] deleteTESTS = {
            {CONTENT_URI + "/" + 0, null, null, null},
            {CONTENT_URI + "/" + 1, null, null, null}};
    private static final Object[][] insertTESTS = {
            {CONTENT_URI, insertTESTS_values[0]},
            {CONTENT_URI, insertTESTS_values[1]},
            {CONTENT_URI, insertTESTS_values[2]},
            {CONTENT_URI, insertTESTS_values[3]}};
    private static final Object[][] updateTESTS = {
            {Uri.parse(CONTENT_URI + "/" + 1), updateTESTS_values[0]},
            {Uri.parse(CONTENT_URI + "/" + 1), updateTESTS_values[1]},
            {Uri.parse(CONTENT_URI + "/" + 1), updateTESTS_values[2]},
            {Uri.parse(CONTENT_URI + "/" + 1), updateTESTS_values[3]}};

    @Override
    public Object[][] getTests(Method m) throws NotTestableException {
        Object[][] tests;
        try {
            Class<?> c = this.getClass();
            if (m == c.getMethod("insert", Uri.class, ContentValues.class)) {
                tests = insertTESTS;
            } else if (m == c.getMethod("delete", Uri.class, String.class, String[].class)) {
                tests = deleteTESTS;
            } else if (m == c.getMethod("update", Uri.class, ContentValues.class, String.class, String[].class)) {
                tests = updateTESTS;
            } else {
                throw new NotTestableException("Method " + m.getName() + " does not have any test implemented");
            }
            return tests;
        } catch (NoSuchMethodException e) {
            throw new NotTestableException("Class " + this.getClass().getName() + " does not include method " + m.getName());
        }
    }*/
}
