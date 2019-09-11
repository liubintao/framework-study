package com.robust.study.netty.lion.api.spi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 8:42
 * @Version: 1.0
 */
public final class SpiLoader {
    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    public static void clear() {
        CACHE.clear();
    }

    public static <T> T load(Class<T> tClass) {
        return load(tClass, null);
    }

    private static <T> T load(Class<T> tClass, String name) {
        String key = tClass.getName();
        Object o = CACHE.get(key);

        if (o == null) {
            T t = load0(tClass, name);
            if (t != null) {
                CACHE.put(key, t);
                return t;
            }
        } else if (tClass.isInstance(o)) {
            return (T) o;
        }
        return load0(tClass, name);
    }

    private static <T> T load0(Class<T> tClass, String name) {
        ServiceLoader<T> factories = ServiceLoader.load(tClass);
        T t = filterByName(factories, name);

        if (t == null) {
            factories = ServiceLoader.load(tClass, SpiLoader.class.getClassLoader());
            t = filterByName(factories, name);
        }

        if (t != null) {
            return t;
        } else {
            throw new IllegalStateException("Cannot find META-INF/services/" + tClass.getName() + " on classpath");
        }
    }

    private static <T> T filterByName(ServiceLoader<T> factories, String name) {
        Iterator<T> iterator = factories.iterator();
        if (name == null) {
            List<T> list = new ArrayList<>(2);
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }

            if (list.size() > 1) {
                list.sort((o1, o2) -> {
                    SPI spi1 = o1.getClass().getAnnotation(SPI.class);
                    SPI spi2 = o2.getClass().getAnnotation(SPI.class);

                    int order1 = spi1 == null ? 0 : spi1.order();
                    int order2 = spi2 == null ? 0 : spi2.order();

                    return order1 - order2;
                });
            }

            if (list.size() > 0) {
                return list.get(0);
            }
        } else {
            while (iterator.hasNext()) {
                T t = iterator.next();
                if (name.equals(t.getClass().getName()) || name.equals(t.getClass().getSimpleName())) {
                    return t;
                }
            }
        }
        return null;
    }


}
