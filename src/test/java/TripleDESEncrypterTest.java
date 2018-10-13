import crypto.TripleDESEncrypter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class TripleDESEncrypterTest {

    private TripleDESEncrypter tripleDESEncrypter;
    private ClassLoader classLoader;

    @Before
    public void init() {
        tripleDESEncrypter = TripleDESEncrypter.getInstance();
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void canEncryptText() {
        String plainText = "PLAIN TEXT";
        String expectedEncryptedText = "rYBGRizCb+9ifEUBw7lKDQ==";
        String encryptionResult = tripleDESEncrypter.encrypt(plainText);
        assertEquals(expectedEncryptedText,encryptionResult);
    }

    @Test
    public void canDecryptText() {
        String expectedDecryptedText = "PLAIN TEXT";
        String encryptedText = "rYBGRizCb+9ifEUBw7lKDQ==";
        String decryptionResult = tripleDESEncrypter.decrypt(encryptedText);
        assertEquals(expectedDecryptedText,decryptionResult);
    }

    @Test
    public void canEncryptFile() {
        File sourceFile = new File(classLoader.getResource("plainTextFile.txt").getFile());
        File destFile = new File(classLoader.getResource("encryptedFile.txt").getFile());

        tripleDESEncrypter.encryptFile(sourceFile.getPath(),destFile.getPath());
    }

    @Test
    public void canDecryptFile() {
        File sourceFile = new File(classLoader.getResource("encryptedFile.txt").getFile());
        File destFile = new File(classLoader.getResource("decryptedFile.txt").getFile());

        tripleDESEncrypter.decryptFile(sourceFile.getPath(),destFile.getPath());
    }


}
