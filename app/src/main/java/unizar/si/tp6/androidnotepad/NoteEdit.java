package unizar.si.tp6.androidnotepad;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.note.Category;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NoteEdit extends ActionBarActivity {
    // TODO: mostrar categoría seleccionada
    private final Category NEW_CATEGORY = new Category(null);
    private final Category SELECTED_CATEGORY = new Category(null);
    private EditText mTitleText;
    private EditText mBodyText;
    private Uri noteUri;
    private LoaderManager.LoaderCallbacks<Cursor> categoriesLoader;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_note_edit);

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleText.getText().toString();
                if (title.isEmpty()) {
                    makeToast(getString(R.string.title_cannot_be_empty), Toast.LENGTH_LONG);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = {NotesTable.COLUMN_TITLE, NotesTable.COLUMN_CATEGORY, NotesTable.COLUMN_BODY};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null) {
                cursor.moveToFirst();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_TITLE));
                mTitleText.setText(title);
                String category = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                SELECTED_CATEGORY.setName(category);
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
        String category = SELECTED_CATEGORY.getName();
        String body = mBodyText.getText().toString();
        if (!title.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(NotesTable.COLUMN_TITLE, title);
            if (category == null) {
                values.putNull(NotesTable.COLUMN_CATEGORY);
            } else {
                values.put(NotesTable.COLUMN_CATEGORY, category);
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_categories:
                selectCategories();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectCategories() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select categories for the note");
        Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/CATEGORIES");
        String[] projection = {NotesTable.COLUMN_ID, NotesTable.COLUMN_CATEGORY};
        final Cursor categoriesCursor = getContentResolver().query(uri, projection, null, null, null);
        final MatrixCursor extrasCursor = new MatrixCursor(projection);
        if (NEW_CATEGORY.getName() != null && SELECTED_CATEGORY.getName().equals(NEW_CATEGORY.getName())) {
            extrasCursor.addRow(new Object[]{-1, NEW_CATEGORY.getName()});
        }
        extrasCursor.addRow(new Object[]{-2, getString(R.string.new_category)});
        final MergeCursor extendedCursor = new MergeCursor(new Cursor[]{categoriesCursor, extrasCursor});
        DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == extendedCursor.getCount() - 1) {
                    newCategory();
                    dialog.dismiss();
                } else {
                    extendedCursor.moveToPosition(which);
                    String category = extendedCursor.getString(extendedCursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                    SELECTED_CATEGORY.setName(category);
                }
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        int selected = -1;
        if (SELECTED_CATEGORY.getName() != null) {
            boolean found = false;
            for (int i = 0; extendedCursor.moveToNext() && !found; i++) {
                String category = extendedCursor.getString(categoriesCursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                if (SELECTED_CATEGORY.getName().equals(category)) {
                    found = true;
                    selected = i;
                }
            }
        }
        builder.setSingleChoiceItems(extendedCursor, selected, NotesTable.COLUMN_CATEGORY, selectListener);
        builder.setPositiveButton(R.string.accept, null);
        builder.setNegativeButton(R.string.cancel, cancelListener);
        builder.show();
    }

    private void newCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_category));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NEW_CATEGORY.setName(input.getText().toString());
                SELECTED_CATEGORY.setName(NEW_CATEGORY.getName());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
