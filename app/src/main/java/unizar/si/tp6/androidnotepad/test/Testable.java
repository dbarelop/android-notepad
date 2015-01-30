package unizar.si.tp6.androidnotepad.test;

import java.lang.reflect.Method;

/**
 * Created by dbarelop on 12/12/14.
 */
public interface Testable {
    /**
     * Returns a list of inputs to test against the method 'm'
     *
     * @param m the method the inputs are designed for
     * @return an array containing one array for each set of inputs to test
     * @throws NotTestableException if the method is not testable
     */
    public Object[][] getTests(Method m) throws NotTestableException;
}
