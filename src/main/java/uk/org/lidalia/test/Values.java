package uk.org.lidalia.test;

import java.lang.reflect.Array;
import java.util.Random;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Static utility functions for returning values for testing.
 */
public final class Values {

    private static final Random RANDOM = new Random();

    /**
     * Returns a best effort at a unique value for a given type.
     * <p>
     * Specifically:
     * <ul>
     *     <li>Returns a random value for a primitive type or primitive wrapper type</li>
     *     <li>Returns a random non-empty String for String</li>
     *     <li>Returns an array with one element, whose value is set by a recursive call to this function, for an array</li>
     *     <li>Attempts to return a {@link org.powermock.api.mockito.PowerMockito} mock for any other type</li>
     * </ul>
     * @param aClass the class for which a unique value is required
     * @param <T> the type of the unique value required
     * @return a unique value of the required type
     */
    @SuppressWarnings("unchecked")
    public static <T> T uniqueValueFor(final Class<T> aClass) {
        final Object value;
        if (isBoolean(aClass)) {
            value = RANDOM.nextBoolean();
        } else if (isByte(aClass)) {
            value = (byte) RANDOM.nextInt();
        } else if (isShort(aClass)) {
            value = (short) RANDOM.nextInt(); // NOPMD have to support short
        } else if (isChar(aClass)) {
            value = (char) RANDOM.nextInt();
        } else if (isInt(aClass)) {
            value = RANDOM.nextInt();
        } else if (isLong(aClass)) {
            value = RANDOM.nextLong();
        } else if (isFloat(aClass)) {
            value = RANDOM.nextFloat();
        } else if (isDouble(aClass)) {
            value = RANDOM.nextDouble();
        } else if (aClass == String.class) {
            value = String.valueOf(RANDOM.nextLong());
        } else if (aClass.isArray()) {
            value = Array.newInstance(aClass.getComponentType(), 1);
            Array.set(value, 0, uniqueValueFor(aClass.getComponentType()));
        } else {
            value = mock(aClass);
        }
        return (T) value;
    }

    private static boolean isInt(final Class<?> aClass) {
        return aClass == int.class || aClass == Integer.class;
    }

    private static boolean isChar(final Class<?> aClass) {
        return aClass == char.class || aClass == Character.class;
    }

    private static boolean isShort(final Class<?> aClass) {
        return aClass == short.class || aClass == Short.class; // NOPMD have to support short
    }

    private static boolean isByte(final Class<?> aClass) {
        return aClass == byte.class || aClass == Byte.class;
    }

    private static boolean isDouble(final Class<?> aClass) {
        return aClass == double.class || aClass == Double.class;
    }

    private static boolean isFloat(final Class<?> aClass) {
        return aClass == float.class || aClass == Float.class;
    }

    private static boolean isBoolean(final Class<?> aClass) {
        return aClass == boolean.class || aClass == Boolean.class;
    }

    private static boolean isLong(final Class<?> aClass) {
        return aClass == long.class || aClass == Long.class;
    }

    private Values() {
        throw new UnsupportedOperationException("Not instantiable");
    }
}
