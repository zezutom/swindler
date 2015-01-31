package org.zezutom.swindler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by tom on 29/01/2015.
 */
public class Swindler {


    public static FieldHandler with(String className) {
        try {
            return new FieldHandler(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found " + className);
        }
    }

    public static<T extends Object> FieldHandler with(Class<T> clazz) {
        return new FieldHandler(clazz);
    }

    static class FieldHandler<T extends Object> {
        private Class<T> clazz;

        FieldHandler(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ValueHandler get(String fieldName) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                return new ValueHandler(field);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Couldn't access the requested field");
            }

        }
    }

    static class ValueHandler {

        private Field field;

        public ValueHandler(Field field) {
            this.field = field;
        }

        public<T extends Object> void set(T value) {
            try {
                field.set(null, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't set the requested value " + value);
            }
        }
    }
}
