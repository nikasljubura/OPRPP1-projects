package hr.fer.oprpp1.jmbag0036532652;

/**
 * Defines two conversion methods for the user: byte to hex and hex to byte
 */

public class Util {

    /**
     * method takes hex-encoded String and returns appropriate byte[]
     * For zero-length
     * string, method must return zero-length byte array. It supports both uppercase and lowercase letters.
     * @param keyText text encoded string
     * @return byte array
     * @throws IllegalArgumentException
     */
    public static byte[] hextobyte(String keyText){

        int length = keyText.length();
        byte[] result = new byte[length/2];
        if(length%2 == 0 && length != 0){
            for(int i = 0; i < length; i = i + 2){
                byte b1 = (byte) (Character.digit(keyText.charAt(i), 16) << 4);
                byte b2 = (byte) (Character.digit(keyText.charAt(i+1), 16));
                result[i/2] = (byte) (b1 + b2);
            }
        }else if(length == 0){
            return result;
        }else{
            throw new IllegalArgumentException();
        }

        return result;

    }


    /**
     * Method takes a byte array and creates its hex-encoding: for each byte of given
     * array, two characters are returned in string, in big-endian notation.
     * For zero-length array an empty string must be returned.
     * @param bytearray byte array
     * @return hex encoded String
     */
    public static String bytetohex(byte[] bytearray ){

        StringBuilder strbuilder = new StringBuilder();

        if(bytearray.length != 0){
            for (byte x : bytearray) {
                strbuilder.append(String.format("%02x", x));
            }
        }else{
            return "";
        }

        return strbuilder.toString();

    }

}
