package co.mannit.commonservice.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
public class AESSymmetricEncryption {
	
	private static final Logger logger = LogManager.getLogger(AESSymmetricEncryption.class);
	
	private SecretKey secretKey;
	
	@Value("${encryptionkey}")
	private String encryptionKey;
	
	public SecretKey getSecretKey() {
		return secretKey;
	}

	public AESSymmetricEncryption() throws NoSuchAlgorithmException, InvalidKeySpecException {
		encryptionKey = "{nirm%]#$}";
		logger.debug("constructor called",encryptionKey);
		logger.debug("---init--- encryptionKey{}",encryptionKey);
		secretKey = generateSecretKeyFromPassphrase(encryptionKey);
		logger.debug(secretKey);
	}
	
//	@PostConstruct
//	public void init() throws Exception{
//		logger.debug("---init--- encryptionKey{}",encryptionKey);
//		secretKey = generateSecretKeyFromPassphrase(encryptionKey);
//		logger.debug(secretKey);
//	}

    public static SecretKey generateSecretKeyFromPassphrase(String passphrase)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // You can adjust these parameters as needed
        int iterationCount = 1000;
        int keyLength = 128;

        // Generate a key specification for PBKDF2
        KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), new byte[16], iterationCount, keyLength);

        // Use PBKDF2 to derive the key
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();

        // Create a SecretKey from the derived key bytes
        return new SecretKeySpec(keyBytes, "AES");
    }

    public byte[] encrypt(String plainText, SecretKey secretKey) throws Exception{
        try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(plainText.getBytes());
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			throw new Exception();
		}
    }

    public String encryptAsString(String plainText, SecretKey secretKey) throws Exception{
        return Base64.getEncoder().encodeToString(encrypt(plainText, secretKey));
    }
    
    public String decrypt(byte[] encryptedText, SecretKey secretKey) throws Exception{
        try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedText);
			return new String(decryptedBytes);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			throw new Exception();
		}
    }
    
    public String decrypt(String encryptedText, SecretKey secretKey) throws Exception{
        return decrypt(Base64.getDecoder().decode(encryptedText), secretKey);
    }
    
    public static void main(String[] args) {
        try {
        	AESSymmetricEncryption obj = new AESSymmetricEncryption();
            String passphrase = "MySecretPassphrase";
            String plainText = "Hello, Symmetric Encryption with Passphrase!";

           	// Derive a symmetric key from the passphrase
            SecretKey secretKey = generateSecretKeyFromPassphrase(passphrase);

            // Encrypt data
            byte[] encryptedText = obj.encrypt(plainText, secretKey);

            SecretKey secretKey2 = generateSecretKeyFromPassphrase(passphrase);
            // Decrypt data
            String decryptedText = obj.decrypt(encryptedText, secretKey2);

            System.out.println("Original: " + plainText);
            System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encryptedText));
            System.out.println("Decrypted: " + decryptedText);
            
            AESSymmetricEncryption crypto = new AESSymmetricEncryption();
            crypto.secretKey = generateSecretKeyFromPassphrase("{nirm%]#$}");
            
            System.out.println("plainText "+plainText);
            String strencryptedText = crypto.encryptAsString(plainText, crypto.secretKey);
            System.out.println("Encrypted Value "+strencryptedText);
            String strdecryptedText = crypto.decrypt("HSrNjMaBm4CQeXsa1yMinQ==", crypto.secretKey);
            System.out.println("decryptedText Value "+strdecryptedText);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
