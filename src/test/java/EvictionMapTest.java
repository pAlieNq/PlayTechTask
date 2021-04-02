import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EvictionMapTest {

    EvictionMap<String, Integer> evictionMap = new EvictionMap<String, Integer>(5000);

    @Test
    public void entryExpires() throws InterruptedException {
        evictionMap.put("test1", 1);
        assertNotNull(evictionMap.get("test1"));
        TimeUnit.MILLISECONDS.sleep(5000);
        assertNull(evictionMap.get("test1"));
    }

    @Test
    public void manyEntriesExpire() throws InterruptedException {
        evictionMap.put("test1", 1);
        evictionMap.put("test2", 2);
        evictionMap.put("test3", 3);
        assertNotNull(evictionMap.get("test1"));
        assertNotNull(evictionMap.get("test2"));
        assertNotNull(evictionMap.get("test3"));
        TimeUnit.MILLISECONDS.sleep(5000);
        assertNull(evictionMap.get("test1"));
        assertNull(evictionMap.get("test2"));
        assertNull(evictionMap.get("test3"));
    }

    @Test
    public void resetsExpirationTime() throws InterruptedException {
        evictionMap.put("test1", 1);
        assertNotNull(evictionMap.get("test1"));
        TimeUnit.MILLISECONDS.sleep(1000);
        evictionMap.put("test1", 2);
        TimeUnit.MILLISECONDS.sleep(4000);
        assertNotNull(evictionMap.get("test1"));
        TimeUnit.MILLISECONDS.sleep(1000);
        assertNull(evictionMap.get("test1"));
    }


}