package co.mannit.commonservice.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AESKeyGenerationExample {
    public static void main(String[] args) {
        try {
            // Create a KeyGenerator instance for AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

            // Initialize the key generator with a key size (128, 192, or 256 bits)
            keyGenerator.init(128); // You can change the key size here

            // Generate a random AES key
            SecretKey secretKey = keyGenerator.generateKey();

            // Convert the secret key to bytes (if needed)
            byte[] keyBytes = secretKey.getEncoded();
            System.out.println(new String(keyBytes));

            // Print the AES key (in bytes, encoded as hexadecimal)
            System.out.println("AES Key (Hex): " + bytesToHex(keyBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert bytes to hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
