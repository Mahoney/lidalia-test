package uk.org.lidalia.test;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static uk.org.lidalia.test.Assert.assertNotInstantiable;
import static uk.org.lidalia.test.Values.uniqueValueFor;

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
    public void uniqueValueForByte() throws Exception {
        byte val1 = uniqueValueFor(byte.class);
        byte val2 = uniqueValueFor(byte.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForShort() throws Exception {
        short val1 = uniqueValueFor(short.class);
        short val2 = uniqueValueFor(short.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForChar() throws Exception {
        char val1 = uniqueValueFor(char.class);
        char val2 = uniqueValueFor(char.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForInt() throws Exception {
        int val1 = uniqueValueFor(int.class);
        int val2 = uniqueValueFor(int.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForLong() throws Exception {
        long val1 = uniqueValueFor(long.class);
        long val2 = uniqueValueFor(long.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForFloat() throws Exception {
        float val1 = uniqueValueFor(float.class);
        float val2 = uniqueValueFor(float.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForDouble() throws Exception {
        double val1 = uniqueValueFor(double.class);
        double val2 = uniqueValueFor(double.class);
        assertFalse(val1 == val2);
    }

    @Test
    public void uniqueValueForByteObject() throws Exception {
        Byte val1 = uniqueValueFor(Byte.class);
        Byte val2 = uniqueValueFor(Byte.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForShortObject() throws Exception {
        Short val1 = uniqueValueFor(Short.class);
        Short val2 = uniqueValueFor(Short.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForCharacter() throws Exception {
        Character val1 = uniqueValueFor(Character.class);
        Character val2 = uniqueValueFor(Character.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForInteger() throws Exception {
        Integer val1 = uniqueValueFor(Integer.class);
        Integer val2 = uniqueValueFor(Integer.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForLongObject() throws Exception {
        Long val1 = uniqueValueFor(Long.class);
        Long val2 = uniqueValueFor(Long.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForFloatObject() throws Exception {
        Float val1 = uniqueValueFor(Float.class);
        Float val2 = uniqueValueFor(Float.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForDoubleObject() throws Exception {
        Double val1 = uniqueValueFor(Double.class);
        Double val2 = uniqueValueFor(Double.class);
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
    public void uniqueValueForString() throws Exception {
        String val1 = uniqueValueFor(String.class);
        String val2 = uniqueValueFor(String.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void uniqueValueForInterface() throws Exception {
        Serializable val1 = uniqueValueFor(Serializable.class);
        Serializable val2 = uniqueValueFor(Serializable.class);
        assertFalse(val1.equals(val2));
    }

    @Test
    public void notInstantiable() throws Throwable {
        assertNotInstantiable(Values.class);
    }
}
