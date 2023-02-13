package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkedListIndexedCollectionTest {


    /**
     * Tests the size method
     */
    @Test
    public void testSize(){

        LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
        col1.add("A");

        assertEquals(1,col1.size());
    }




    /**
     * Tests the second constructor
     */
    @Test
    public void testConstructor2(){

        LinkedListIndexedCollection testCol = new LinkedListIndexedCollection();
        testCol.add("Mala");
        testCol.add("Mecka");
        testCol.add("Jezic");
        testCol.add("Misic");


        LinkedListIndexedCollection arr = new LinkedListIndexedCollection((Collection) testCol);

        assertEquals(testCol.size(),arr.size());
    }


    /**
     * tests adding null element to collection
     */
    @Test
    public void testAddNullArgument(){

        LinkedListIndexedCollection testCol = new LinkedListIndexedCollection();
        assertThrows(NullPointerException.class,()->testCol.add(null));
    }



    /**
     * tests adding to collection
     */
    @Test
    public void testAdd(){

        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        col.add("Mala");
        col.add("Macka");

        String[] tst = new String[]{"Mala", "Macka"};

        assertArrayEquals(tst, col.toArray());
    }



    /**
     * tests illegal index passed
     */
    @Test
    public void testIllegalIndex(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> tst.get(-1));
    }

    /**
     * tests getting element by index
     */
    @Test
    public void testGetByIndex(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");

        assertEquals("B", tst.get(1));
    }


    /**
     * tests clearing the collection
     */
    @Test
    public void testClearCollection(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.clear();

        assertEquals(0, tst.size());
    }

    /**
     * tests invalid index insertion
     */
    @Test
    public void testInvalidIndexPosition1(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> tst.insert(1, -1));
    }

    /**
     * tests invalid index insertion
     */
    @Test
    public void testInvalidIndexPosition2(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> tst.insert(1, 18));
    }

    /**
     * tests actual insertion
     */
    @Test
    public void testInsertion(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("x");
        tst.add("y");
        tst.add("z");
        tst.add("w");

        String[] arr = new String[]{"a", "x", "y", "z", "w"};

        tst.insert("a", 0);

        assertArrayEquals(arr, tst.toArray());

    }


    /**
     * tests finding null element
     */
    @Test
    public void testIndexOfNull(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();

        assertEquals(-1, tst.indexOf(null));
    }

    /**
     * tests finding element
     */
    @Test
    public void testIndexOfEl(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");

        assertEquals(1, tst.indexOf("B"));
    }



    /**
     * tests empty collection
     */
    @Test
    public void testEmptyCollection(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        assertEquals(true, tst.isEmpty());


    }

    /**
     * tests not empty collection
     */
    @Test
    public void testNotEmptyCollection(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");
        assertEquals(false, tst.isEmpty());


    }

    /**
     * tests containes object
     */

    @Test
    public void testContainesObject(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");
        assertEquals(true, tst.contains("A"));


    }

    /**
     * tests doesnt contain object
     */

    @Test
    public void testDoesntContainesObject(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");
        assertEquals(false, tst.contains("C"));


    }

    /**
     * tests conversion to array
     */

    @Test
    public void testConversionToArray(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");
        String[] arr = new String[]{"A", "B"};

        assertArrayEquals(arr, tst.toArray());


    }





    @Test
    public void testRemovesObject(){

        LinkedListIndexedCollection tst = new LinkedListIndexedCollection();
        tst.add("A");
        tst.add("B");

        assertEquals(true, tst.remove("B"));


    }



}
