package simplersa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class SimpleRSA {

    public static void main(String[] args) throws IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        BufferedReader ch = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Enter message to encode: ");
            String toEncode = ch.readLine();

            char[] toCodeChars = toEncode.toCharArray();

            byte[] toCodeBytes = new byte[toCodeChars.length];

            for (int i = 0; i < toCodeChars.length; i++) {
                toCodeBytes[i] = (byte) toCodeChars[i];
            }

            //διαδικασια κρυπτογραφησης
            // δημιουργια κλειδιου
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);// 
            KeyPair keyPair = keyGen.generateKeyPair();

            System.out.print("--Private Key--\n");
            System.out.print(DatatypeConverter.printHexBinary(keyPair.getPrivate().getEncoded()) + "\n");
            System.out.println(keyPair.getPrivate().getEncoded().length);
            System.out.print("--Public Key--\n");
            System.out.print(DatatypeConverter.printHexBinary(keyPair.getPublic().getEncoded()) + "\n");
            System.out.println(keyPair.getPublic().getEncoded().length);

            //Αρχικοποιηση  κλειδιου σε encrypt mode
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());

            byte[] Coded = new byte[toCodeBytes.length];

            //διαδικασια κρυπτογραφησης
            Coded = cipher.doFinal(toCodeBytes);

            StringBuffer hexCipher = new StringBuffer();

            for (int i = 0; i < Coded.length; i++) {
                hexCipher.append(Integer.toHexString(0xFF & Coded[i]));
            }
            System.out.println("\n");
            System.out.println("Cipher Text: " + hexCipher);

            Cipher cipher1 = Cipher.getInstance("RSA");

            cipher1.init(Cipher.DECRYPT_MODE, keyPair.getPublic());

            byte[] Decoded = cipher1.doFinal(Coded);//αποκρυπτογραφηση

            char[] DecodedChars = new char[Decoded.length];

            for (int i = 0; i < Decoded.length; i++) {
                DecodedChars[i] = (char) Decoded[i];
            }

            System.out.println("Deciphered Text: " + new String(DecodedChars));

        } catch (NoSuchAlgorithmException ex) {

            Logger.getLogger(SimpleRSA.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

}
