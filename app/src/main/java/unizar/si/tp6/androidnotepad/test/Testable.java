package unizar.si.tp6.androidnotepad.test;

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
    public Tests<?> getTests();
}
