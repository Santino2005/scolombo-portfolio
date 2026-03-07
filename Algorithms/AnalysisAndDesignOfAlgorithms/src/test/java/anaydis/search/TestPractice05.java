package anaydis.search;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class TestPractice05 {
    private TSTTrieMap<String> map;


    @Test
    public void testPutAndGet() {
        map = new TSTTrieMap<>();
        map.put("hello", "pipo");
        map.put("hello", "pepinaso");
        Assert.assertEquals("pepinaso", map.get("hello"));
        Assert.assertNull(map.get("helloo"));
        Assert.assertNull(map.get("world"));
    }

    @Test
    public void testContainsKey() {
        map = new TSTTrieMap<>();
        map.put("foo", "bar");
        Assert.assertTrue(map.containsKey("foo"));
        Assert.assertFalse(map.containsKey("fo"));
        Assert.assertFalse(map.containsKey("bar"));
    }

    @Test
    public void testSize() {
        map = new TSTTrieMap<>();
        map.put("apple", "fruit");
        map.put("banana", "fruit");
        Assert.assertEquals(2, map.size());
        map.put("apple", "green fruit");
        Assert.assertEquals(2, map.size());
    }

    @Test
    public void testClear() {
        map = new TSTTrieMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Assert.assertEquals(2, map.size());
        map.clear();
        Assert.assertEquals(0, map.size());
        Assert.assertFalse(map.containsKey("key1"));
        Assert.assertFalse(map.containsKey("key2"));
    }



    @Test
    public void testKeys() {
        map = new TSTTrieMap<>();
        map.put("cat", "animal");
        map.put("car", "vehicle");
        map.put("dog", "animal");

        Iterator<String> keys = map.keys();
        Assert.assertTrue(keys.hasNext());
        Assert.assertEquals("car", keys.next());
        Assert.assertTrue(keys.hasNext());
        Assert.assertEquals("cat", keys.next());
        Assert.assertTrue(keys.hasNext());
        Assert.assertEquals("dog", keys.next());
    }

    private BinaryTrieMap<String> Bmap;
    @Test
    public void testPutAndGetBTM() {
        Bmap = new BinaryTrieMap<>();
        Bmap.put("hello", "pipo");
        Bmap.put("hello", "pepinaso");
        Bmap.put("coco", "fruit");
        Assert.assertEquals("pepinaso", Bmap.get("hello"));
        Assert.assertNull(Bmap.get("helloo"));
        Assert.assertNull(Bmap.get("world"));
    }

    @Test
    public void testContainsKeyBTM() {
        Bmap = new BinaryTrieMap<>();
        Bmap.put("foo", "bar");
        Assert.assertTrue(Bmap.containsKey("foo"));
        Assert.assertFalse(Bmap.containsKey("fo"));
        Assert.assertFalse(Bmap.containsKey("bar"));
    }

    @Test
    public void testSizeBTM() {
        Bmap = new BinaryTrieMap<>();
        Assert.assertEquals(0, Bmap.size());
        Bmap.put("apple", "fruit");
        Bmap.put("banana", "fruit");
        Assert.assertEquals(2, Bmap.size());
        Bmap.put("apple", "green fruit");
        Assert.assertEquals(2, Bmap.size());
    }

    @Test
    public void testClearBTM() {
        Bmap = new BinaryTrieMap<>();
        Bmap.put("key1", "value1");
        Bmap.put("key2", "value2");
        Assert.assertEquals(2, Bmap.size());
        Bmap.clear();
        Assert.assertEquals(0, Bmap.size());
        Assert.assertFalse(Bmap.containsKey("key1"));
        Assert.assertFalse(Bmap.containsKey("key2"));
    }
    @Test
    public void testKeysBTM() {
        Bmap = new BinaryTrieMap<>();
        Bmap.put("abc", "value1");
        Bmap.put("abd", "value2");
        Bmap.put("aacd", "value3");

        Iterator<String> iterator = Bmap.keys();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("abd", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("abc", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("aacd", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
    private RWayTrieMap<String> Rmap;
    @Test
    public void testPutAndGetRWT() {
        Rmap = new RWayTrieMap<>();
        Rmap.put("abc", "value1");
        Rmap.put("abd", "value2");
        Rmap.put("aacd", "value3");

        Assert.assertEquals("value1", Rmap.get("abc"));
        Assert.assertEquals("value2", Rmap.get("abd"));
        Assert.assertEquals("value3", Rmap.get("aacd"));
        Assert.assertNull(Rmap.get("non"));
    }

    @Test
    public void testContainsKeyRWT() {
        Rmap = new RWayTrieMap<>();
        Rmap.put("abc", "value1");
        Rmap.put("abd", "value2");

        Assert.assertTrue(Rmap.containsKey("abc"));
        Assert.assertTrue(Rmap.containsKey("abd"));
        Assert.assertFalse(Rmap.containsKey("aacd"));
    }

    @Test
    public void testSizeRWT() {
        Rmap = new RWayTrieMap<>();
        Assert.assertEquals(0, Rmap.size());

        Rmap.put("abc", "value1");
        Rmap.put("abd", "value2");

        Assert.assertEquals(2, Rmap.size());
    }

    @Test
    public void testClearRWT() {
        Rmap = new RWayTrieMap<>();
        Rmap.put("abc", "value1");
        Rmap.put("abd", "value2");
        Assert.assertEquals(2, Rmap.size());

        Rmap.clear();
        Assert.assertTrue(Rmap.isEmpty());
        Assert.assertEquals(0, Rmap.size());
    }

    @Test
    public void testKeysRWT() {
        Rmap = new RWayTrieMap<>();
        Rmap.put("abc", "value1");
        Rmap.put("abd", "value2");
        Rmap.put("aacd", "value3");

        Iterator<String> iterator = Rmap.keys();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("aacd", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("abc", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("abd", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}

