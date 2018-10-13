package crypto;

import config.AppConfig;
import file.FileManager;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

import static java.util.Objects.isNull;

public class TripleDESEncrypter {
    private static final String UNICODE_FORMAT = "UTF8";
    private Cipher cipher;
    private SecretKey secretKey;
    private static final String CIPHER_TYPE = "DESede";

    private final static String encryptionKey = "ThisIsSecretKeyForTheApp";
    private static TripleDESEncrypter instance;

    private TripleDESEncrypter() throws Exception {
        cipher = Cipher.getInstance(CIPHER_TYPE);
        KeySpec keySpec = new DESedeKeySpec(encryptionKey.getBytes(UNICODE_FORMAT));
        secretKey = SecretKeyFactory.getInstance(CIPHER_TYPE).generateSecret(keySpec);
    }

    public static TripleDESEncrypter getInstance() {
        if (isNull(instance)) {
            try {
                return new TripleDESEncrypter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;

    }


    public String encrypt(String plainText) {
        String encryptedText = new String("");
        try {
            initCipher(Cipher.ENCRYPT_MODE);
            byte[] plainTextBytes = plainText.getBytes(UNICODE_FORMAT);
            byte[] encryptedTextBytes = cipher.doFinal(plainTextBytes);
            encryptedText = new String(Base64.encodeBase64(encryptedTextBytes));

        } catch (Exception e) {
            e.printStackTrace();
        }
        FileManager.getInstance().saveToFile(encryptedText, new File(AppConfig.TEXT_ENCRYPTION_OUTPUT_FILE_PATH));
        return encryptedText;
    }


    public String decrypt(String encryptedString) {
        String decryptedText = new String("");
        try {
            initCipher(Cipher.DECRYPT_MODE);
            byte[] encryptedTextBytes = Base64.decodeBase64(encryptedString);
            byte[] plainTextBytes = cipher.doFinal(encryptedTextBytes);
            decryptedText = new String(plainTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public void encryptFile(String sourceFilePath, String outputFilePath) {
        try {
            initCipher(Cipher.ENCRYPT_MODE);

            FileInputStream in = new FileInputStream(sourceFilePath);
            FileOutputStream out = new FileOutputStream(outputFilePath);

            CipherInputStream cis = new CipherInputStream(in, cipher);
            doCopy(cis, out);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    public void decryptFile(String encryptedFilePath, String outputFilePath) {
        try {
            initCipher(Cipher.DECRYPT_MODE);

            FileInputStream in = new FileInputStream(encryptedFilePath);
            FileOutputStream out = new FileOutputStream(outputFilePath);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            doCopy(in, cos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCipher(int mode) {
        try {
            cipher.init(mode, secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
