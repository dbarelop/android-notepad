package unizar.si.tp6.androidnotepad.test;

/**
 * Created by dbarelop on 1/2/15.
 */
public abstract class Test<C extends Testable> {
    /**
     * Performs the test this class defines to the instance of the Testable class 'instance'
     *
     * @param instance The instance of the Testable class to perform the test to
     * @throws Exception
     */
    public abstract void performTest(C instance) throws Exception;

    /**
     * Returns the title of the test
     *
     * @return The title of the test
     */
    public abstract String getTitle();

    /**
     * Returns the description of the test
     * @return The description of the test
     */
    public abstract String getDescription();

    /**
     * Returns the time the test took to run
     * @return Returns the time the test took to run
     */
    public abstract long getExecutionTime();

    @Override
    public String toString() {
        return getTitle();
    }
}
