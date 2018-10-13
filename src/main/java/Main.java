import com.mysql.cj.util.StringUtils;
import config.AppConfig;
import controller.CryptoOperationsController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    private static JTextArea textArea;
    private static JPanel panel;
    private static JTextField textField;
    private static JButton encryptBtn;
    private static JButton selectFileToDecryptBtn;
    private static JButton selectFileToEncryptBtn;
    private static JButton encryptFileToDatabaseBtn;
    private static JButton decryptFileFromDatabaseBtn;
    private static JButton programPresentationBtn;
    private static CryptoOperationsController cryptoOperationsController = new CryptoOperationsController();
    private static final JFileChooser fileChooser = new JFileChooser();

    public static void main(String[] args) throws Exception {
        initView();

    }

    private static void initView() {
        JFrame frame = new JFrame("Aplikacja szyfrująca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

        createBottomPanel();
        textArea = new JTextArea();

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, textArea);
        frame.setVisible(true);


    }

    private static void encryptTextAndPrintResult(String plainText) {
        String encryptedText = cryptoOperationsController.encryptText(plainText);
        textArea.append("Szyfruję tekst : " + plainText + "\n");
        textArea.append("Zaszyfrowano wprowadzony tekst do pliku " + AppConfig.getTextEncryptionOutputFilePath() + "\n");
        textArea.append("Zaszyfrowany tekst : " + encryptedText + "\n");
        textArea.append("Odszyfrowywanie...\n");
        textArea.append("Odszyfrowany tekst : " + cryptoOperationsController.decryptText(encryptedText) + "\n");
    }

    private static void createBottomPanel() {
        panel = new JPanel();
        JLabel enterTextLabel = new JLabel("Wprowadz tekst");
        textField = new JTextField(30);
        encryptBtn = new JButton("Zaszyfruj");
        JButton resetBtn = new JButton("Wyczyść");
        selectFileToEncryptBtn = new JButton("Zaszyfruj plik");
        selectFileToDecryptBtn = new JButton("Deszyfruj plik");
        encryptFileToDatabaseBtn = new JButton("Zaszyfruj plik do bazy danych");
        decryptFileFromDatabaseBtn = new JButton("Odszyfruj plik z bazy danych");
        programPresentationBtn = new JButton("Samograj");
        panel.add(enterTextLabel);
        panel.add(enterTextLabel);
        panel.add(textField);
        panel.add(encryptBtn);
        panel.add(resetBtn);
        panel.add(selectFileToEncryptBtn);
        panel.add(selectFileToDecryptBtn);
        panel.add(encryptFileToDatabaseBtn);
        panel.add(decryptFileFromDatabaseBtn);
        panel.add(programPresentationBtn);

        initButtons();

    }

    private static void initButtons() {
        encryptBtn.addActionListener(ae -> {
            final String plainText = textField.getText();
            if (plainText != null && !plainText.isEmpty()) {
                encryptTextAndPrintResult(plainText);
            }
        });

        selectFileToEncryptBtn.addActionListener(ae -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                encryptFile(file);

            }

        });

        selectFileToDecryptBtn.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                decryptFile(file);

            }
        });

        encryptFileToDatabaseBtn.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    saveEncryptedInDB(file);
                } catch (IOException e) {
                    textArea.append("Błąd podczas szyfrowania pliku do bazy danych \n");
                }

            }
        });

        decryptFileFromDatabaseBtn.addActionListener(a -> {
            List<String> fileNames = cryptoOperationsController.getAllEncryptedInDBFileNames();
            String selectedFileName = (String) JOptionPane.showInputDialog(null, "Wybierz plik",
                    "Wybór pliku", JOptionPane.QUESTION_MESSAGE, null, fileNames.toArray(), null);

            if (!StringUtils.isNullOrEmpty(selectedFileName)) {
                try {
                    decryptFileFromDB(selectedFileName);
                } catch (IOException e) {
                    textArea.append("Błąd podczas szyfrowania pliku do bazy danych \n");
                }
            }
        });

        programPresentationBtn.addActionListener(a -> {

            textArea.append("--------- PREZENTACJA APLIKACJI -----------------\n");
            textArea.append("SZYFROWANIE TEKSTU : \n");

            String plainText = "TEKST JAWNY";
            textField.setText(plainText);
            encryptTextAndPrintResult(plainText);

            textArea.append("-------------------------------------------\n");

            textArea.append("SZYFROWANIE I DESZYFROWANIE PLIKU : \n");

            File plainTextFile = new File(AppConfig.getPlainTextFilePath());
            encryptFile(plainTextFile);

            File encryptedFile = new File(AppConfig.getEncryptedFilePath());
            decryptFile(encryptedFile);

            textArea.append("ZAPIS SZYFROWANEGO PLIKU DO BAZY DANYCH \n");


            try {
                textArea.append("Szyfruję plik " + plainTextFile.getName() + " do bazy danych \n");
                saveEncryptedInDB(plainTextFile);

                textArea.append("DESZYFROWANIE PLIKU ZAPISANEGO W BAZIE DANYCH \n");

                textArea.append("Deszyfruję plik " + plainTextFile.getName() + " z bazy danych \n");
                decryptFileFromDB("plainTextFile");

            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    private static void decryptFileFromDB(String selectedFileName) throws IOException {
        cryptoOperationsController.decryptFileFromDB(selectedFileName);
        textArea.append("Odszyfrowany plik " + selectedFileName + " znajduje się w katalogu "
                + AppConfig.getDecryptedFilesFromDbPath() + "\n");
    }

    private static void saveEncryptedInDB(File file) throws IOException {
        cryptoOperationsController.encryptFileToDatabase(file);
        textArea.append("Zapisano zaszyfrowany plik " + file.getName() + " do bazy danych \n");
    }

    private static void decryptFile(File file) {
        cryptoOperationsController.decryptFile(file);
        textArea.append("Odszyfrowano plik " + file.getName() + " do pliku " + AppConfig.getDecryptedFilePath() + "\n");
    }

    private static void encryptFile(File file) {
        cryptoOperationsController.encryptFile(file);
        textArea.append("Zaszyfrowano plik " + file.getName() + " do pliku " + AppConfig.getEncryptedFilePath() + "\n");
    }

}
