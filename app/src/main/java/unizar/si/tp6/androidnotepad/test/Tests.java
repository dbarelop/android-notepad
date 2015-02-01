package unizar.si.tp6.androidnotepad.test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Created by dbarelop on 1/2/15.
 */
public abstract class Tests<T extends Testable> {
    private Map<Method, Collection<Test<T, ? extends Method>>> testsCollection;

    public Tests() {
        initializeTests();
    }

    protected abstract void initializeTests();

    public Collection<Test<T, ? extends Method>> getTests(Method m) throws NotTestableException {
        Collection<Test<T, ? extends Method>> requestedTests = testsCollection.get(m);
        if (requestedTests == null) {
            throw new NotTestableException("Method " + m.getName() + " does not have any test implemented");
        } else {
            return requestedTests;
        }
    }
}
