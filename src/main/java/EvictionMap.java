import java.util.concurrent.*;

public class EvictionMap<K, V> extends ConcurrentHashMap<K, V> {

    private Long durationInNanos;
    private ScheduledExecutorService scheduler;
    private ConcurrentHashMap<K, Long> evictionTimeMap;
    private ConcurrentLinkedQueue<K> evictionQueue;

    public EvictionMap(long durationInMillis) {
        this.durationInNanos = TimeUnit.MILLISECONDS.toNanos(durationInMillis);
        this.evictionTimeMap = new ConcurrentHashMap<K, Long>();
        this.evictionQueue = new ConcurrentLinkedQueue<K>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() { public void run() {

            for (K k : evictionQueue) {
                if (evictionTimeMap.get(k) < System.nanoTime()) {
                    remove(k);
                    evictionQueue.remove();
                } else {
                    break;
                }
            }
        }}, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    synchronized public V put(K key, V value) {
        evictionTimeMap.put(key, System.nanoTime() + durationInNanos);
        evictionQueue.add(key);
        return super.put(key, value);
    }

    @Override
    synchronized public V get(Object key) {
        if (evictionTimeMap.get(key) != null && evictionTimeMap.get(key) < System.nanoTime()) {
            remove(key);
        }
        return super.get(key);
    }

}
