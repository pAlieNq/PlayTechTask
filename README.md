EvictionMap extends ConcurrentHashMap to provide thread-safety.
In my solution I used ScheduledExecutorService to find expired entries.
Every second it iterates through the queue of keys, contained in EvictionMap, and checks if expiration time (in nanoseconds) of entry is smaller than current nano time.
If it is, entry is deleted from the EvictionMap.
