package unizar.si.tp6.androidnotepad.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

import unizar.si.tp6.androidnotepad.R;

public class TestsList extends ActionBarActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_list);

        listView = (ListView) findViewById(R.id.tests_list);
        AdapterView.OnItemClickListener testsListItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> testClass = (Class<?>) parent.getItemAtPosition(position);
                try {
                    if (Testable.class.isAssignableFrom(testClass)) {
                        Testable instance = ((Testable) testClass.newInstance()).getInstance(getApplicationContext());
                        showTestMethodsDialog(instance);
                    }
                } catch (InstantiationException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                } catch (IllegalAccessException e) {
                }
            }
        };
        listView.setOnItemClickListener(testsListItemClickListener);
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_categories_list_item, R.id.categories_list_item, Testable.TESTABLE_CLASSES);
        listView.setAdapter(adapter);
    }

    private <T extends Testable> void showTestMethodsDialog(final T instance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_method));
        final Class<?> testClass = instance.getClass();
        final Method[] methods = testClass.getDeclaredMethods();
        CharSequence[] methodsNames = new CharSequence[methods.length];
        for (int i = 0; i < methods.length; i++) methodsNames[i] = methods[i].getName();
        DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Method testMethod = methods[which];
                try {
                    List<? extends Test<? extends Testable>> testCollection = instance.getTests().getTests(testMethod);
                    showTestsForMethodDialog((List<Test<T>>) testCollection, instance);
                } catch (NotTestableException e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
        builder.setItems(methodsNames, selectListener);
        builder.show();
    }

    private <T extends Testable> void showTestsForMethodDialog(final List<Test<T>> testCollection, final T instance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_test));
        CharSequence[] testsNames = new CharSequence[testCollection.size()];
        for (int i = 0; i < testsNames.length; i++)
            testsNames[i] = testCollection.get(i).toString();
        DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Test<T> test = testCollection.get(which);
                showTestDialog(test, instance);
            }
        };
        builder.setItems(testsNames, selectListener);
        builder.show();
    }

    private <T extends Testable> void showTestDialog(final Test<T> test, final T instance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(test.getTitle());
        builder.setMessage(test.getDescription());
        DialogInterface.OnClickListener acceptListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TestsList.this);
                try {
                    test.performTest(instance);
                    builder.setTitle(getString(R.string.test_succesful));
                } catch (Exception e) {
                    builder.setTitle(getString(R.string.test_not_success));
                    builder.setMessage(e.getMessage());
                }
                builder.show();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        builder.setPositiveButton(getString(R.string.perform_test), acceptListener);
        builder.setNegativeButton(R.string.cancel, cancelListener);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tests_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }
}
