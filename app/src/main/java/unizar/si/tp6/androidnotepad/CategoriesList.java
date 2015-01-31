package unizar.si.tp6.androidnotepad;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesTable;


public class CategoriesList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int CONTEXT_MENU_DELETE_ID = Menu.FIRST;

    private SimpleCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        listView = (ListView) findViewById(R.id.manage_categories);
        AdapterView.OnCreateContextMenuListener categoriesListContextMenuListener = new AdapterView.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CONTEXT_MENU_DELETE_ID, 0, R.string.context_menu_delete);
            }
        };
        AdapterView.OnItemClickListener categoriesListItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                editCategory(position);
            }
        };
        listView.setOnCreateContextMenuListener(categoriesListContextMenuListener);
        listView.setOnItemClickListener(categoriesListItemClickListener);
        fillData();
    }

    private void editCategory(int position) {
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);
        final String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_category_name));
        final EditText input = new EditText(this);
        builder.setView(input);
        final LoaderManager.LoaderCallbacks<Cursor> callback = this;
        DialogInterface.OnClickListener acceptListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategory = input.getText().toString();
                if (TextUtils.isEmpty(newCategory)) {
                    // TODO: no cerrar diálogo (?)
                    makeToast("New category name can't be empty", Toast.LENGTH_LONG);
                } else {
                    Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/CATEGORY/" + category);
                    ContentValues values = new ContentValues();
                    values.put(NotesTable.COLUMN_CATEGORY, newCategory);
                    getContentResolver().update(uri, values, null, null);
                    getLoaderManager().restartLoader(0, null, callback);
                }
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        builder.setPositiveButton(getString(R.string.accept), acceptListener);
        builder.setNegativeButton(getString(R.string.cancel), cancelListener);
        builder.show();
    }

    private void fillData() {
        String[] from = {NotesTable.COLUMN_CATEGORY};
        int[] to = {R.id.categories_list_item};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_categories_list_item, null, from, to, 0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categories_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // sin acciones por ahora
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Cursor c = adapter.getCursor();
                c.moveToPosition(info.position);
                String category = c.getString(c.getColumnIndexOrThrow(NotesTable.COLUMN_CATEGORY));
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/CATEGORY/" + category);
                ContentValues values = new ContentValues();
                values.putNull(NotesTable.COLUMN_CATEGORY);
                getContentResolver().update(uri, values, null, null);
                getLoaderManager().restartLoader(0, null, this);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/CATEGORIES");
        String[] projection = {NotesTable.COLUMN_ID, NotesTable.COLUMN_CATEGORY};
        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void makeToast(String message, int length) {
        Toast.makeText(this, message, length).show();
    }
}
