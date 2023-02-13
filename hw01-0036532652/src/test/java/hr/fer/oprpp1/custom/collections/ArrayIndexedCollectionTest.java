package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    /**
     * Tests to see if constructor throws an exception when we try to pass illegal argument as size
     */
    @Test
    public void testCapacityTooSmall(){
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
    }


    /**
     * Tests to see if constructor throws an exception when we pass collection that is null
     */
    @Test
    public void testCollectionIsNull1() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));

    }

    /**
     * Tests the other constructor
     */
    @Test
    public void testCollectionIsNull2() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 16));

    }



    /**
     * Tests the size method
     */
    @Test
    public void testSize(){
        ArrayIndexedCollection testCol = new ArrayIndexedCollection(2);
        testCol.add("A");
        assertEquals(1, testCol.size());
    }




    /**
     * Tests to see if the constructed collection is the same size as given, when given a smaller initial size
     * that the size of the passed collection (reallocation testing)
     */
    @Test
    public void testInitialCapacitySmallerThanCollection(){

        ArrayIndexedCollection testCol = new ArrayIndexedCollection(4);
        testCol.add("Mala");
        testCol.add("Mecka");
        testCol.add("Jezic");
        testCol.add("Misic");


        ArrayIndexedCollection arr = new ArrayIndexedCollection((Collection) testCol, 3);

        assertEquals(testCol.size(),arr.size());
    }


    /**
     * tests adding null element to collection
     */
    @Test
    public void testAddNullArgument(){

        ArrayIndexedCollection testCol = new ArrayIndexedCollection(4);
        assertThrows(NullPointerException.class,()->testCol.add(null));
    }



    /**
     * tests adding to not full array
     */
    @Test
    public void testArrayNotFull(){

        ArrayIndexedCollection col = new ArrayIndexedCollection(2);
        col.add("Mala");
        col.add("Macka");

        String[] tst = new String[]{"Mala", "Macka"};

        assertArrayEquals(tst, col.toArray());
    }


    /**
     * tests adding to full array -> should reallocate
     */
    @Test
    public void testArrayFull(){

        ArrayIndexedCollection col = new ArrayIndexedCollection(1);
        col.add("Mala");
        col.add("Macka");

        assertEquals(2,col.size());
    }

    /**
     * tests illegal index passed
     */
    @Test
    public void testIllegalIndex(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);
        assertThrows(IndexOutOfBoundsException.class, () -> tst.get(-1));
    }

    /**
     * tests getting element by index
     */
    @Test
    public void testGetByIndex(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);
        tst.add("A");
        tst.add("B");

        assertEquals("B", tst.get(1));
    }


    /**
     * tests clearing the collection
     */
    @Test
    public void testClearCollection(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);
        tst.add("A");
        tst.clear();

        assertEquals(0, tst.size());
    }

    /**
     * tests invalid index insertion
     */
    @Test
    public void testInvalidIndexPosition1(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);
        assertThrows(IndexOutOfBoundsException.class, () -> tst.insert(1, -1));
    }

    /**
     * tests invalid index insertion
     */
    @Test
    public void testInvalidIndexPosition2(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);
        assertThrows(IndexOutOfBoundsException.class, () -> tst.insert(1, 18));
    }

    /**
     * tests actual insertion
     */
    @Test
    public void testInsertion(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(4);
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

        ArrayIndexedCollection tst = new ArrayIndexedCollection(12);

        assertEquals(-1, tst.indexOf(null));
    }

    /**
     * tests finding element
     */
    @Test
    public void testIndexOfEl(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");

        assertEquals(1, tst.indexOf("B"));
    }



    /**
     * tests empty collection
     */
    @Test
    public void testEmptyCollection(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        assertEquals(true, tst.isEmpty());


    }

    /**
     * tests not empty collection
     */
    @Test
    public void testNotEmptyCollection(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");
        assertEquals(false, tst.isEmpty());


    }

    /**
     * tests containes object
     */

    @Test
    public void testContainesObject(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");
        assertEquals(true, tst.contains("A"));


    }

    /**
     * tests doesnt contain object
     */

    @Test
    public void testDoesntContainesObject(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");
        assertEquals(false, tst.contains("C"));


    }

    /**
     * tests conversion to array
     */

    @Test
    public void testConversionToArray(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");
        String[] arr = new String[]{"A", "B"};

        assertArrayEquals(arr, tst.toArray());


    }


    /**
     * Procesor test...
     */



    @Test
    public void testRemovesObject(){

        ArrayIndexedCollection tst = new ArrayIndexedCollection(2);
        tst.add("A");
        tst.add("B");

        assertEquals(true, tst.remove("B"));


    }



}
