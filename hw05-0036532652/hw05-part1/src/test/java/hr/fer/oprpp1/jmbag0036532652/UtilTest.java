package hr.fer.oprpp1.jmbag0036532652;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UtilTest {

    @Test
    public void testHexToByte(){

        byte[] expected = new byte[]{1, -82, 34};

        assertArrayEquals(expected, Util.hextobyte("01aE22"));
    }

    @Test
    public void testHexToByteEmpty(){

        byte[] expected = new byte[]{};

        assertArrayEquals(expected, Util.hextobyte(""));
    }

    @Test
    public void testHexToByteException1(){
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("01aE2"));
    }

    @Test
    public void testHexToByteException2(){
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("ž"));
    }

    @Test
    public void testByteToHexNotEmpty(){

        assertEquals("01ae22", Util.bytetohex(new byte[] {1, -82, 34}));

    }

    @Test
    public void testByteToHexEmpty(){

        assertEquals("", Util.bytetohex(new byte[] {}));

    }


}
