package co.mannit.commonservice.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AESEncryptionAndDecryption {
	public static void main(String[] args) {
	    try {
	        // Generate a symmetric key (AES in this example)
	        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	        keyGenerator.init(128); // You can change the key size (128, 192, or 256 bits)
	        SecretKey secretKey = keyGenerator.generateKey();

	        
	        // Encrypt data
	        String plainText = "Hello, Symmetric Encryption!";
	        byte[] encryptedText = encrypt(plainText, secretKey);
	
	        String encodedString = Base64.getEncoder().encodeToString(encryptedText);
	        System.out.println("Encrypted value as String "+encodedString);
	        
	        // Decrypt data
	        String decryptedText = decrypt(Base64.getDecoder().decode(encodedString), secretKey);
	
	        System.out.println("Original: " + plainText);
	        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encryptedText));
	        System.out.println("Decrypted: " + decryptedText);
	    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
	        e.printStackTrace();
	    }
	}
	
	public static byte[] encrypt(String plainText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	    return cipher.doFinal(plainText.getBytes());
	}
	
	public static String decrypt(byte[] encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	    byte[] decryptedBytes = cipher.doFinal(encryptedText);
	    return new String(decryptedBytes);
	}
	
	public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	    byte[] decryptedBytes = cipher.doFinal(encryptedText.getBytes());
	    return new String(decryptedBytes);
	}
}
