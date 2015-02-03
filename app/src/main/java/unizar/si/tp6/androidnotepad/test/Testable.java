package unizar.si.tp6.androidnotepad.test;

import android.content.Context;

import unizar.si.tp6.androidnotepad.contentprovider.NotesContentProvider;

/**
 * Created by dbarelop on 1/2/15.
 */
public interface Testable {
    // TODO: obtener automáticamente
    public final static Class<?>[] TESTABLE_CLASSES = {NotesContentProvider.class};

    /**
     * Returns a Tests class containing all the programmed tests for the class that implements
     * this interface
     *
     * @return A Tests class containing all the programmed tests for the class that implements
     * this interface
     */
    public TestCollection<?> getTests();

    /**
     * Returns an instance of the class, preconfigured to be used by a test
     *
     * @param context The application context
     * @return An instance of the class, preconfigured to be used by a test
     */
    public Testable getInstance(Context context);
}
