package com.sejong.sejongpeer.util;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;

public class DomainObjectUtil {
    public static <T> T createInstance(Class<T> clazz, Consumer<T> fieldInitializer) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            T instance = constructor.newInstance();

            if (fieldInitializer != null) {
                fieldInitializer.accept(instance);
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Test failed with: e=" + e.getMessage());
        }
    }
}
