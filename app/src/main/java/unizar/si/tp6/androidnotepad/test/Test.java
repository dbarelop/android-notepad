package unizar.si.tp6.androidnotepad.test;

import java.lang.reflect.Method;

/**
 * Created by dbarelop on 1/2/15.
 */
public abstract class Test<C extends Testable, M extends Method> {
    public abstract void performTest();
}
