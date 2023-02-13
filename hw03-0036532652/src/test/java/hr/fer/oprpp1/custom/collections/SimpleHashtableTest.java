package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleHashtableTest {

    @Test
    public void testSize(){

        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(2, map.size());
    }

    @Test
    public void testContainesKey(){
        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(true, map.containsKey(3));

    }

    @Test
    public void testContainesValue(){
        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(false, map.containsKey(6));

    }

    @Test
    public void testRemove(){
        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(3, map.remove(3));
    }

    @Test
    public void isEmpty() {
        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(false, map.isEmpty());
    }

    @Test
    public void testSize2(){
        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);
        map.remove(3);

        assertEquals(1, map.size());
    }

    @Test
    public void testGet(){

        SimpleHashtable<Integer, Integer> map = new SimpleHashtable<>(4);
        map.put(3,3);
        map.put(4,5);

        assertEquals(3, map.get(3));
    }


    //put works -> tested in main



}
