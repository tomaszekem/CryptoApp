package crypto;

import config.AppConfig;
import db.entity.EncryptedData;
import file.FileManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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

    public EncryptedData encryptFile(File file) throws IOException {
        initCipher(Cipher.ENCRYPT_MODE);
        File tempFile = File.createTempFile("tempfile", ".tmp");

        FileInputStream in = new FileInputStream(file.getAbsoluteFile());
        FileOutputStream out = new FileOutputStream(tempFile);

        CipherInputStream cis = new CipherInputStream(in, cipher);
        doCopy(cis, out);

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        EncryptedData encryptedData = new EncryptedData(IOUtils.toByteArray(fileInputStream), file);
        fileInputStream.close();
        tempFile.delete();
        return encryptedData;
    }

    public void decryptFile(EncryptedData encryptedData) throws IOException {
//        File encryptedFile = new File(AppConfig.ENCRYPTED_FILE_FROM_DB_PATH + encryptedData.getFileName() + encryptedData.getFileExtension());
        File encryptedFile = File.createTempFile("tempfile", ".tmp");
        FileUtils.writeByteArrayToFile(encryptedFile, encryptedData.getData());
        File file = new File(AppConfig.DECRYPTED_FILES_FROM_DB_PATH + encryptedData.getFileNameWithExtension());
        decryptFile(encryptedFile.getAbsolutePath(), file.getAbsolutePath());
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
