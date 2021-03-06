package unizar.si.tp6.androidnotepad;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.email.MailAbstraction;
import unizar.si.tp6.androidnotepad.email.MailAbstractionImpl;
import unizar.si.tp6.androidnotepad.note.Category;
import unizar.si.tp6.androidnotepad.note.Note;


public class NotesList extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderManager.LoaderCallbacks<Cursor> {
    private final int CONTEXT_MENU_DELETE_ID = Menu.FIRST;
    private final int CONTEXT_MENU_EMAIL_ID = Menu.FIRST + 1;

    private SimpleCursorAdapter adapter;
    private ListView listView;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        listView = (ListView) findViewById(R.id.notes_list);
        listView.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CONTEXT_MENU_DELETE_ID, 0, R.string.context_menu_delete);
                menu.add(0, CONTEXT_MENU_EMAIL_ID, 0, R.string.context_menu_email);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                editNote(id);
            }
        });
        fillData();
    }

    private void fillData() {
        String[] from = {NotesTable.COLUMN_TITLE};
        int[] to = {R.id.notes_list_item};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_notes_list_item, null, from, to, 0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                return true;
            case CONTEXT_MENU_EMAIL_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                MailAbstraction email = new MailAbstractionImpl(this);
                uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + info.id);
                Note n = null;
                try (Cursor c = getContentResolver().query(uri, null, null, null, null)) {
                    c.moveToFirst();
                    n = Note.fetchNote(c);
                }
                String subject = n == null ? null : n.getTitle();
                String body = n == null ? null : n.getBody();
                try {
                    email.send(subject, body);
                } catch (Exception e) {
                    Log.d(NotesList.class.getName(), e.toString());
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
    }

    public void onSectionAttached(int number) {
        if (mNavigationDrawerFragment != null) {
            selectedCategory = mNavigationDrawerFragment.getCategoryAtPosition(number);
            getLoaderManager().restartLoader(0, null, this);
            mTitle = selectedCategory == null ? getString(R.string.all_categories) : selectedCategory.toString();
            getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.notes_list, menu);
            getSupportActionBar().setTitle(mTitle);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_note:
                createNote();
                return true;
            case R.id.manage_categories:
                manageCategories();
                return true;
            case R.id.delete_all:
                deleteAllNotes();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivity(i);
    }

    private void manageCategories() {
        Intent i = new Intent(this, CategoriesList.class);
        startActivity(i);
    }

    private void deleteAllNotes() {
        Uri uri = NotesContentProvider.CONTENT_URI;
        getContentResolver().delete(uri, null, null);
    }

    private void editNote(long id) {
        Intent i = new Intent(this, NoteEdit.class);
        Uri notesUri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(NotesContentProvider.CONTENT_ITEM_TYPE, notesUri);
        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NotesContentProvider.CONTENT_URI;
        uri = selectedCategory != null ? Uri.parse(uri + "/CATEGORY/" + selectedCategory) : uri;
        String[] projection = {NotesTable.COLUMN_ID, NotesTable.COLUMN_TITLE};
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NotesList) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
