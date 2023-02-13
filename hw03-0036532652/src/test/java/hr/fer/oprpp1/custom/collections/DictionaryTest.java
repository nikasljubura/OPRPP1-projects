package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DictionaryTest {

    @Test
    public void testSize(){

       Dictionary<Integer, Integer> dict = new Dictionary<>();

       dict.put(3,3);

       assertEquals(1, dict.size());

    }

    @Test
    public void testEmptiness(){
        Dictionary<Integer, Integer> dict = new Dictionary<>();

        dict.put(3,3);

        assertEquals(false, dict.isEmpty());

    }

    @Test
    public void clearTest(){

        Dictionary<Integer, Integer> dict = new Dictionary<>();

        dict.put(3,3);
        dict.clear();

        assertEquals(true, dict.isEmpty());

    }

    @Test
    public void put1Test(){

        Dictionary<Integer, Integer> dict = new Dictionary<>();
        dict.put(3,3);
        dict.put(4, null);


        assertEquals(null, dict.put(4,5));

    }

    @Test
    public void put2Test(){

        Dictionary<Integer, Integer> dict = new Dictionary<>();
        dict.put(3,3);
        dict.put(4, 5);
        dict.put(4,6);



        assertEquals(6, dict.get(4));

    }


    @Test
    public void put3Test(){

        Dictionary<Integer, Integer> dict = new Dictionary<>();
        dict.put(3,3);
        dict.put(4,6);


        assertEquals(6, dict.put(4,5));

    }

    @Test
    public void testRemove(){

        Dictionary<Integer, Integer> dict = new Dictionary<>();
        dict.put(3,3);
        dict.put(4, 5);

        dict.remove(4);
        assertEquals(1, dict.size());

    }



}
