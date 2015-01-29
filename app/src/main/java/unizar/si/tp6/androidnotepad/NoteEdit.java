package unizar.si.tp6.androidnotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesDatabaseHelper;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.note.Category;

/**
 * Created by dbarelop on 05/01/15.
 */
public class NoteEdit extends Activity {
    private Spinner mCategory;
    private EditText mTitleText;
    private EditText mBodyText;
    private Uri notesUri;
    private ArrayAdapter<String> categoriesAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_note_edit);

        mCategory = (Spinner) findViewById(R.id.category_spinner);
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        Button confirmButton = (Button) findViewById(R.id.confirm);

        NotesDatabaseHelper dbHelper = new NotesDatabaseHelper(this);
        List<String> categories = new ArrayList<>();
        for(Category c : dbHelper.getCategories()) {
            categories.add(c.toString());
        }
        categories.add("New category");
        categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, android.R.id.text1, categories);
        mCategory.setAdapter(categoriesAdapter);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if (item.equals("New category")) {
                    newCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle extras = getIntent().getExtras();
        if(bundle != null || getIntent().getExtras() != null) {
            notesUri = extras.getParcelable(NotesContentProvider.CONTENT_ITEM_TYPE);
            fillData(notesUri);
        } else {
            notesUri = null;
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast("Title field cannot be empty", Toast.LENGTH_LONG);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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
                String newCategory = input.getText().toString();
                categoriesAdapter.add(newCategory);
                categoriesAdapter.notifyDataSetChanged();
                mCategory.setSelection(categoriesAdapter.getPosition(newCategory));
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
        String[] projection = { NotesTable.COLUMN_TITLE, NotesTable.COLUMN_CATEGORY, NotesTable.COLUMN_BODY };
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if(cursor != null) {
                cursor.moveToFirst();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_TITLE));
                mTitleText.setText(title);
                String category = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                for(int i = 0; i < mCategory.getCount(); i++) {
                    String item = (String) mCategory.getItemAtPosition(i);
                    if(item.equals(category)) {
                        mCategory.setSelection(i);
                    }
                }
                String body = cursor.getString(cursor.getColumnIndexOrThrow(NotesTable.COLUMN_BODY));
                mBodyText.setText(body);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(NotesContentProvider.CONTENT_ITEM_TYPE, notesUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String category = (String) mCategory.getSelectedItem();
        String body = mBodyText.getText().toString();
        if(!title.isEmpty() || !body.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(NotesTable.COLUMN_TITLE, title);
            values.put(NotesTable.COLUMN_CATEGORY, category);
            values.put(NotesTable.COLUMN_BODY, body);
            if(notesUri == null) {
                notesUri = getContentResolver().insert(NotesContentProvider.CONTENT_URI, values);
            } else {
                getContentResolver().update(notesUri, values, null, null);
            }
        }
    }

    private void makeToast(String message, int length) {
        Toast.makeText(this, message, length).show();
    }
}
