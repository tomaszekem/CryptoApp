package controller;

import config.AppConfig;
import crypto.TripleDESEncrypter;
import db.entity.EncryptedData;
import db.entity.service.EncryptedDataService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CryptoOperationsController {

    private TripleDESEncrypter tripleDESEncrypter = TripleDESEncrypter.getInstance();
    private EncryptedDataService encryptedDataService = EncryptedDataService.getInstance();

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

    public void encryptFileToDatabase(File file) throws IOException {
        encryptedDataService.encryptAndSave(file);
    }

    public List<String> getAllEncryptedInDBFileNames() {
        return encryptedDataService.getAllEncryptedFileNames();
    }

    public void decryptFileFromDB(String fileName) throws IOException {
        EncryptedData encryptedData = encryptedDataService.getByFileName(fileName);
        tripleDESEncrypter.decryptFile(encryptedData);
    }

}
