package controller;

import config.AppConfig;
import crypto.TripleDESEncrypter;

import java.io.File;

public class CryptoOperationsController {

    private TripleDESEncrypter tripleDESEncrypter = TripleDESEncrypter.getInstance();

    public String encryptText(String text) {
        return tripleDESEncrypter.encrypt(text);
    }

    public String decryptText(String enryptedText) {
        return tripleDESEncrypter.decrypt(enryptedText);
    }

    public void encryptFile(File file) {
        tripleDESEncrypter.encryptFile(file.getAbsolutePath(), AppConfig.ENCRYPTED_FILE_PATH);
    }

    public void decryptFile(File encryptedFile) {
        tripleDESEncrypter.decryptFile(encryptedFile.getAbsolutePath(), AppConfig.DECRYPTED_FILE_PATH);
    }


}
