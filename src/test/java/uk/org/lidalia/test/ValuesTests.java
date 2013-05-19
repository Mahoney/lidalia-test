package uk.org.lidalia.test;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static uk.org.lidalia.test.Assert.isNotInstantiable;
import static uk.org.lidalia.test.Values.uniqueValueFor;

@RunWith(JUnitParamsRunner.class)
public class ValuesTests {

    @Test
    public void uniqueValueForBoolean() throws Exception {
        boolean initialValue = uniqueValueFor(boolean.class);
        for (int i = 0; i < 100; i++) {
            boolean otherValue = uniqueValueFor(boolean.class);
            if (otherValue != initialValue) {
                return;
            }
        }
        fail("Always returns the same value!");
    }

    @Test
    public void uniqueValueForBooleanObject() throws Exception {
        boolean initialValue = uniqueValueFor(Boolean.class);
        for (int i = 0; i < 100; i++) {
            boolean otherValue = uniqueValueFor(Boolean.class);
            if (otherValue != initialValue) {
                return;
            }
        }
        fail("Always returns the same value!");
    }

    public Object[] types() {
        return $(
                byte.class,
                short.class,
                char.class,
                int.class,
                long.class,
                float.class,
                double.class,
                Byte.class,
                Short.class,
                Character.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                Serializable.class,
                String.class
        );
    }

    @Test
    @Parameters(method = "types")
    public void uniqueValueForType(Class<?> type) {
        Object val1 = uniqueValueFor(type);
        Object val2 = uniqueValueFor(type);
        assertFalse(val1 == val2);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForPrimitiveArray() throws Exception {
        int[] val1 = uniqueValueFor(int[].class);
        int[] val2 = uniqueValueFor(int[].class);
        assertFalse(Arrays.equals(val1, val2));
    }

    @Test
    public void uniqueValueForObjectArray() throws Exception {
        Object[] val1 = uniqueValueFor(Object[].class);
        Object[] val2 = uniqueValueFor(Object[].class);
        assertFalse(Arrays.equals(val1, val2));
    }

    @Test
    public void uniqueValueForMultidimensionalObjectArray() throws Exception {
        Object[][] val1 = uniqueValueFor(Object[][].class);
        Object[][] val2 = uniqueValueFor(Object[][].class);
        assertFalse(Arrays.deepEquals(val1, val2));
    }

    @Test
    public void notInstantiable() throws Throwable {
        assertThat(Values.class, isNotInstantiable());
    }
}
