package uk.org.lidalia.test;

import java.lang.reflect.Array;
import java.util.Random;

import static org.powermock.api.mockito.PowerMockito.mock;

public class Values {

    @SuppressWarnings("unchecked")
    public static <T> T uniqueValueFor(Class<T> aClass) {
        Object value;
        Random r = new Random();
        if (aClass == boolean.class || aClass == Boolean.class) {
            value = r.nextBoolean();
        } else if (aClass == byte.class || aClass == Byte.class) {
            value = (byte) r.nextInt();
        } else if (aClass == short.class || aClass == Short.class) {
            value = (short) r.nextInt();
        } else if (aClass == char.class || aClass == Character.class) {
            value = (char) r.nextInt();
        } else if (aClass == int.class || aClass == Integer.class) {
            value = r.nextInt();
        } else if (aClass == long.class || aClass == Long.class) {
            value = r.nextLong();
        } else if (aClass == float.class || aClass == Float.class) {
            value = r.nextFloat();
        } else if (aClass == double.class || aClass == Double.class) {
            value = r.nextDouble();
        } else if (aClass == String.class) {
            value = String.valueOf(new Random().nextLong());
        } else if (aClass.isArray()) {
            value = Array.newInstance(aClass.getComponentType(), 1);
            Array.set(value, 0, uniqueValueFor(aClass.getComponentType()));
        } else {
            value = mock(aClass);
        }
        return (T) value;
    }

    private Values() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
