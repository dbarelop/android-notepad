package unizar.si.tp6.androidnotepad.test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by dbarelop on 1/2/15.
 */
public abstract class Tests<T extends Testable> {
    private Map<Method, List<Test<T>>> testsCollection;

    public Tests() {
        testsCollection = initializeTests();
    }

    protected abstract Map<Method, List<Test<T>>> initializeTests();

    /**
     * Returns all the programmed tests for a given method of the parameterized Testable class 'T'
     *
     * @param m The method to fetch the tests for
     * @return A collection of tests for the given method appliable to an instance of class 'T'
     * @throws NotTestableException If the method is not testable
     */
    public List<Test<T>> getTests(Method m) throws NotTestableException {
        List<Test<T>> requestedTests = testsCollection.get(m);
        if (requestedTests == null) {
            throw new NotTestableException("Method " + m.getName() + " does not have any test implemented");
        } else {
            return requestedTests;
        }
    }
}
