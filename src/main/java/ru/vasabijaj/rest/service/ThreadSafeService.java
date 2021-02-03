package ru.vasabijaj.rest.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class ThreadSafeService<K, V> {
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ConcurrentHashMap<K,V> cache = new ConcurrentHashMap<>();

    public synchronized Future<V> compute(K key, Function<K, V> func) {
        return executor.submit(() -> {
            V value = cache.get(key);
            if (value != null)
                return value;

            value = func.apply(key);
            cache.put(key, value);
            return value;
        });
    }
}
