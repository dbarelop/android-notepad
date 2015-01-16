package unizar.si.tp6.androidnotepad.test;

import java.lang.reflect.Method;

/**
 * Created by dbarelop on 12/12/14.
 */
public interface Testable {
    public Object[][] getTests(Method m) throws NotTestableException;
}
