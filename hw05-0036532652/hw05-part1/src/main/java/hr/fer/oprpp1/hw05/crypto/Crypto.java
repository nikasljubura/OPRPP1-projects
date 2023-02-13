package hr.fer.oprpp1.hw05.crypto;

import hr.fer.oprpp1.jmbag0036532652.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.SQLOutput;

import static hr.fer.oprpp1.jmbag0036532652.Util.hextobyte;

/**
 * class containes methods for encryption, decryption and digesting
 * its main program gathers the needed parameters from user
 */
public class Crypto {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        if(args[0].equals("checksha")){

            String input = args[1];
            Path p = Paths.get(input); //get path

            System.out.println("Please provide expected sha-256 for " + input + ":");
            System.out.print("> ");
            String line = reader.readLine();
            String digest = getDigest(p);
            System.out.print("Digesting completed. ");

            if(digest.equals(line)){
                System.out.println("Digest of " + input + " matches the expected digest. ");
            }else{
                System.out.println("Digest of " + input + " does not match the expected digest. Digest was: " +
                        digest);
            }

        }else if(args[0].equals("encrypt") || args[0].equals("decrypt")){

            String action = args[0];
            String s1 = args[1]; //clean or crypted
            String s2 = args[2]; //crypted or clean

            Path path1 = Paths.get(s1);
            Path path2 = Paths.get(s2);

            System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex digits):");
            System.out.print("> ");

            String keyText = reader.readLine();

            System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
            System.out.print("> ");

            String ivText = reader.readLine();
            boolean encrypt = action.equals("encrypt");

            SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

            if(action.equals("encrypt")){

                action(cipher, path1, path2);
                System.out.println("Encryption completed." +
                        " Generated file " + s2 + " based on file " + s1 + ".");

            }else if(action.equals("decrypt")){

                action(cipher, path1, path2);
                System.out.println("Decryption completed." +
                        " Generated file " + s2 + " based on file " + s1 + ".");

            }else{//unsupported
                throw new IllegalArgumentException();
            }

        }


    }

    public static String getDigest(Path p) throws IOException, NoSuchAlgorithmException {

        InputStream in = new BufferedInputStream(Files.newInputStream(p));
        MessageDigest sha = MessageDigest.getInstance("SHA-256");

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while ((bytesRead = in.read(buffer)) >= 0){
            for(int i = 0; i < bytesRead; i++){
                sha.update(buffer[i]);
            }

        }

        byte[] hash = sha.digest();

        in.close();

        return Util.bytetohex(hash);


    }

    public static void action(Cipher cipher, Path p1, Path p2) throws IOException,
            BadPaddingException, IllegalBlockSizeException {

        InputStream in = new BufferedInputStream(Files.newInputStream(p1));
        OutputStream out = new BufferedOutputStream((Files.newOutputStream(p2)));

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while ((bytesRead = in.read(buffer)) >= 0){

            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                out.write(output);
            }

        }

        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            out.write(outputBytes);
        }

        in.close();
        out.close();

    }


}
