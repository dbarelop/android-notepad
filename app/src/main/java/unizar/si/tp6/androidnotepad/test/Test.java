package unizar.si.tp6.androidnotepad.test;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by dbarelop on 12/12/14.
 */
public class Test<T extends Testable> implements Runnable {
    private final T obj;
    private final Method m;

    public Test(T obj, Method m) {
        this.obj = obj;
        this.m = m;
    }

    @Override
    public void run() {
        try {
            test(obj, m);
        } catch (NotTestableException e) {
            Log.d(e.getMessage(), e.toString(), e);
        }
    }

    public void test(Testable obj, Method m) throws NotTestableException {
        Object[][] tests = obj.getTests(m);
        performTests(obj, m, tests);
    }

    public static void performTests(Object obj, Method m, Object[][] tests) {
        for(int i = 0; i < tests.length; ++i) {
            try {
                m.invoke(obj, (Object) tests[i]);
            } catch(Exception e) {
                Log.d(e.getMessage(), e.toString(), e);
            }
        }
    }
}
