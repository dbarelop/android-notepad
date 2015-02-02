package unizar.si.tp6.androidnotepad.test.contentprovider;

import android.content.ContentValues;
import android.net.Uri;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;
import unizar.si.tp6.androidnotepad.db.NotesTable;
import unizar.si.tp6.androidnotepad.test.Test;
import unizar.si.tp6.androidnotepad.test.TestCollection;

/**
 * Created by dbarelop on 1/2/15.
 */
public class NotesContentProviderTestCollection extends TestCollection<NotesContentProvider> {
    private final static Test<NotesContentProvider> insert2NulluriParameterTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert NULL uri parameter";
        private final String DESCRIPTION = "Calls insert(Uri uri, ContentValues contentValues) with uri = null";
        private final Uri uri = null;
        private final ContentValues contentValues = new ContentValues();

        {
            contentValues.putNull(NotesTable.COLUMN_TITLE);
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2NullcontentValuesParameterTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert NULL contentValues parameter";
        private final String DESCRIPTION = "Calls insert(Uri uri, ContentValues contentValues) with contentValues = null";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = null;

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2NullTitleTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert NULL title";
        private final String DESCRIPTION = "Inserts a note with NULL title";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = new ContentValues();

        {
            contentValues.putNull(NotesTable.COLUMN_TITLE);
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2NullCategoryTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert NULL category";
        private final String DESCRIPTION = "Inserts a note with NULL category";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = new ContentValues();

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "title");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.putNull(NotesTable.COLUMN_CATEGORY);
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2EmptyTitleTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert empty title";
        private final String DESCRIPTION = "Inserts a note with empty title";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = new ContentValues();

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2EmptyCategoryTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert empty category";
        private final String DESCRIPTION = "Inserts a note with empty category";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = new ContentValues();

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "title");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.insert(uri, contentValues);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> insert2Perform1000Insertions = new Test<NotesContentProvider>() {
        private final String TITLE = "Insert 1000 notes";
        private final String DESCRIPTION = "Inserts 1000 notes";
        private final Uri uri = NotesContentProvider.CONTENT_URI;
        private final ContentValues contentValues = new ContentValues();
        {
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        private int N = 1000;

        @Override
        public void performTest(NotesContentProvider instance) {
            for (int i = 0; i < N; i++) {
                contentValues.put(NotesTable.COLUMN_TITLE, "title" + i);
                instance.insert(uri, contentValues);
            }
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> update4NullTitleTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Update title NULL";
        private final String DESCRIPTION = "Updates note with id 0 and sets title to NULL";
        private final Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + 0);
        private final ContentValues contentValues = new ContentValues();
        private final String selection = null;
        private final String[] selectionArgs = null;

        {
            contentValues.putNull(NotesTable.COLUMN_TITLE);
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.update(uri, contentValues, selection, selectionArgs);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> update4NullCategoryTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Update category NULL";
        private final String DESCRIPTION = "Updates note with id 0 and sets category to NULL";
        private final Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + 0);
        private final ContentValues contentValues = new ContentValues();
        private final String selection = null;
        private final String[] selectionArgs = null;

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "title");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.putNull(NotesTable.COLUMN_CATEGORY);
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.update(uri, contentValues, selection, selectionArgs);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> update4EmptyTitleTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Update title empty";
        private final String DESCRIPTION = "Updates note with id 0 and sets title to empty string";
        private final Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + 0);
        private final ContentValues contentValues = new ContentValues();
        private final String selection = null;
        private final String[] selectionArgs = null;

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "test");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.update(uri, contentValues, selection, selectionArgs);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };
    private final static Test<NotesContentProvider> update4EmptyCategoryTest = new Test<NotesContentProvider>() {
        private final String TITLE = "Update category empty";
        private final String DESCRIPTION = "Updates note with id 0 and sets category to empty string";
        private final Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + 0);
        private final ContentValues contentValues = new ContentValues();
        private final String selection = null;
        private final String[] selectionArgs = null;

        {
            contentValues.put(NotesTable.COLUMN_TITLE, "title");
            contentValues.put(NotesTable.COLUMN_BODY, "body");
            contentValues.put(NotesTable.COLUMN_CATEGORY, "");
        }

        @Override
        public void performTest(NotesContentProvider instance) {
            instance.update(uri, contentValues, selection, selectionArgs);
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    };

    @Override
    protected Map<Method, List<Test<NotesContentProvider>>> initializeTests() {
        Map<Method, List<Test<NotesContentProvider>>> testsCollection = new HashMap<>();
        for (Method m : NotesContentProvider.class.getDeclaredMethods()) {
            List<Test<NotesContentProvider>> c = new ArrayList<>();
            try {
                if (m.equals(NotesContentProvider.class.getDeclaredMethod("insert", Uri.class, ContentValues.class))) {
                    c.add(insert2NulluriParameterTest);
                    c.add(insert2NullcontentValuesParameterTest);
                    c.add(insert2NullTitleTest);
                    c.add(insert2NullCategoryTest);
                    c.add(insert2EmptyTitleTest);
                    c.add(insert2EmptyCategoryTest);
                    c.add(insert2Perform1000Insertions);
                } else if (m.equals(NotesContentProvider.class.getDeclaredMethod("delete", Uri.class, String.class, String[].class))) {

                } else if (m.equals(NotesContentProvider.class.getDeclaredMethod("update", Uri.class, ContentValues.class, String.class, String[].class))) {
                    c.add(update4NullTitleTest);
                    c.add(update4NullCategoryTest);
                    c.add(update4EmptyTitleTest);
                    c.add(update4EmptyCategoryTest);
                }
            } catch (NoSuchMethodException e) {
            }
            if (!c.isEmpty()) {
                testsCollection.put(m, c);
            }
        }
        return testsCollection;
    }
}
