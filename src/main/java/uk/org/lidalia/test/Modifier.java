package uk.org.lidalia.test;

import java.lang.reflect.Member;

public enum Modifier {

    PUBLIC {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isPublic(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isPublic(modifiable.getModifiers());
        }
    },

    PRIVATE {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isPrivate(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isPrivate(modifiable.getModifiers());
        }
    },

    PROTECTED {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isProtected(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isProtected(modifiable.getModifiers());
        }
    },

    STATIC {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isStatic(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isStatic(modifiable.getModifiers());
        }
    },

    FINAL {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isFinal(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isFinal(modifiable.getModifiers());
        }
    },

    SYNCHRONIZED {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isSynchronized(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isSynchronized(modifiable.getModifiers());
        }
    },

    VOLATILE {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isVolatile(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isVolatile(modifiable.getModifiers());
        }
    },

    TRANSIENT {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isTransient(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isTransient(modifiable.getModifiers());
        }
    },

    NATIVE {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isNative(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isNative(modifiable.getModifiers());
        }
    },

    INTERFACE {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isInterface(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isInterface(modifiable.getModifiers());
        }
    },

    ABSTRACT {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isAbstract(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isAbstract(modifiable.getModifiers());
        }
    },

    STRICT {
        @Override
        public boolean isTrueOf(final Member modifiable) {
            return java.lang.reflect.Modifier.isStrict(modifiable.getModifiers());
        }

        @Override
        public boolean isTrueOf(final Class<?> modifiable) {
            return java.lang.reflect.Modifier.isStrict(modifiable.getModifiers());
        }
    };

    public abstract boolean isTrueOf(Member modifiable);
    public abstract boolean isTrueOf(Class<?> modifiable);
}
