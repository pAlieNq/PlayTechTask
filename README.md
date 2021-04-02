EvictionMap extends ConcurrentHashMap to provide thread-safety.
In my solution I used ScheduledExecutorService to find expired entries.
Every second it iterates through the queue of keys, contained in EvictionMap, and checks if expiration time (in nanoseconds) of entry is smaller than current nano time.
If it is, entry is deleted from the EvictionMap.
EvictionMap has evictionTimeMap to save expiration time of entry. If value of any entry is updated, evictionTimeMap is also updated with new expiration time for given entry.
I have added check for V get(Object key) method to be sure it returns null value if entry's expiration time is lower than current nano time.
