import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class EvictionMap<K, V> extends ConcurrentHashMap<K, V> {

    private AtomicLong durationInNanos;
    private ScheduledExecutorService scheduler;
    private ConcurrentHashMap<K, Long> evictionTimeMap;

    public EvictionMap(long durationInMillis) {
        this.durationInNanos = new AtomicLong(TimeUnit.MILLISECONDS.toNanos(durationInMillis));
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.evictionTimeMap = new ConcurrentHashMap<K, Long>();
    }

    @Override
    public V put(final K key, V value) {
        evictionTimeMap.put(key, System.nanoTime() + durationInNanos.get());
        schedule(key);
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        if (evictionTimeMap.get(key) < System.nanoTime()) {
            remove(key);
        }
        return super.get(key);
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    synchronized private Future<?> schedule(final K key) {
        return scheduler.schedule(new Runnable() { public void run() {
                                      remove(key); }},
                evictionTimeMap.get(key) - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

}
