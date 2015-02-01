package unizar.si.tp6.androidnotepad.test;

/**
 * Created by dbarelop on 1/2/15.
 */
public abstract class Test<C extends Testable> {
    /**
     * Performs the test this class defines to the instance of the Testable class 'instance'
     *
     * @param instance The instance of the Testable class to perform the test to
     */
    public abstract void performTest(C instance);

    public abstract String getTitle();

    public abstract String getDescription();

    @Override
    public String toString() {
        return getTitle();
    }
}
