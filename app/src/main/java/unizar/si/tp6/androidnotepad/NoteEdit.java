package unizar.si.tp6.androidnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.note.Category;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NoteEdit extends Activity {
    private final Category NEW_CATEGORY = new Category(null);
    private final Category INITIAL_CATEGORY = new Category(null);
    private Spinner mCategory;
    private EditText mTitleText;
    private EditText mBodyText;
    private Uri noteUri;
    private LoaderManager.LoaderCallbacks<Cursor> categoriesLoader;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_note_edit);

        mCategory = (Spinner) findViewById(R.id.category_spinner);
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        Button confirmButton = (Button) findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();
        if (bundle != null || getIntent().getExtras() != null) {
            noteUri = extras.getParcelable(NotesContentProvider.CONTENT_ITEM_TYPE);
            fillData(noteUri);
        } else {
            noteUri = null;
        }

        fillCategoriesSpinner();
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getAdapter().getCount() - 1) {
                    newCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleText.getText().toString();
                Cursor c = (Cursor) mCategory.getSelectedItem();
                String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                if (title.isEmpty()) {
                    makeToast("Title field cannot be empty", Toast.LENGTH_LONG);
                } else if (category.isEmpty()) {
                    makeToast("Category field cannot be empty", Toast.LENGTH_LONG);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillCategoriesSpinner() {
        String[] from = {NotesTable.COLUMN_CATEGORY};
        int[] to = {android.R.id.text1};
        final SimpleCursorAdapter categoriesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null, from, to, 0);
        categoriesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
            private MatrixCursor extrasCursor;

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/CATEGORIES");
                String[] projection = {NotesTable.COLUMN_ID, NotesTable.COLUMN_CATEGORY};
                CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
                extrasCursor = new MatrixCursor(projection);
                if (NEW_CATEGORY.getName() != null) {
                    extrasCursor.addRow(new Object[]{-1, NEW_CATEGORY.getName()});
                } else if (INITIAL_CATEGORY.getName() == null) {
                    extrasCursor.addRow(new Object[]{-1, ""});
                }
                extrasCursor.addRow(new Object[]{-2, "New category"});
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() == 0 && extrasCursor.getCount() == 1) {
                    extrasCursor.addRow(new Object[]{-2, ""});
                }
                MergeCursor extendedCursor = new MergeCursor(new Cursor[]{data, extrasCursor});
                categoriesAdapter.swapCursor(extendedCursor);
                boolean selected = false;
                for (int i = 0; i < categoriesAdapter.getCount() - 1 && !selected; i++) {
                    Cursor c = (Cursor) mCategory.getItemAtPosition(i);
                    String item = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                    if ((NEW_CATEGORY.getName() == null && INITIAL_CATEGORY.getName() != null && INITIAL_CATEGORY.getName().equals(item)) ||
                            (NEW_CATEGORY.getName() != null && NEW_CATEGORY.getName().equals(item)) || i == categoriesAdapter.getCount() - 2) {
                        mCategory.setSelection(i);
                        selected = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                categoriesAdapter.swapCursor(extrasCursor);
            }
        };
        mCategory.setAdapter(categoriesAdapter);
        getLoaderManager().initLoader(0, null, categoriesLoader);
    }

    private void newCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New category");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NEW_CATEGORY.setName(input.getText().toString());
                getLoaderManager().restartLoader(0, null, categoriesLoader);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void fillData(Uri uri) {
        String[] projection = {NotesTable.COLUMN_TITLE, NotesTable.COLUMN_CATEGORY, NotesTable.COLUMN_BODY};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null) {
                cursor.moveToFirst();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_TITLE));
                mTitleText.setText(title);
                String category = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                INITIAL_CATEGORY.setName(category);
                String body = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_BODY));
                mBodyText.setText(body);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(NotesContentProvider.CONTENT_ITEM_TYPE, noteUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        Cursor c = (Cursor) mCategory.getSelectedItem();
        String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
        String body = mBodyText.getText().toString();
        if (!title.isEmpty() && !category.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(NotesTable.COLUMN_TITLE, title);
            values.put(NotesTable.COLUMN_CATEGORY, category);
            values.put(NotesTable.COLUMN_BODY, body);
            if (noteUri == null) {
                noteUri = getContentResolver().insert(NotesContentProvider.CONTENT_URI, values);
            } else {
                getContentResolver().update(noteUri, values, null, null);
            }
        }
    }

    private void makeToast(String message, int length) {
        Toast.makeText(this, message, length).show();
    }
}
