import config.AppConfig;
import controller.CryptoOperationsController;
import crypto.TripleDESEncrypter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Main {

    private static JTextArea textArea;
    private static JPanel panel;
    private static JTextField textField;
    private static JButton encryptBtn;
    private static JButton selectFileToDecryptBtn;
    private static JButton selectFileToEncryptBtn;
    private static CryptoOperationsController cryptoOperationsController = new CryptoOperationsController();
    private static final JFileChooser fileChooser = new JFileChooser();

    public static void main(String[] args) throws Exception {
        initView();


//        TripleDESEncrypter tripleDESEncrypter = TripleDESEncrypter.getInstance();
//        handleFileEncrypting(tripleDESEncrypter);

    }

    private static void initView() {
        //Creating the Frame
        JFrame frame = new JFrame("Aplikacja szyfrująca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

        //Creating the MenuBar and adding components
        JMenuBar menuBar = createMenuBar();
        createBottomPanel();
        textArea = new JTextArea();
        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, textArea);
        frame.setVisible(true);


    }

    private static void encryptFile() {

    }

    private static void encryptTextAndPrintResult(String plainText) {
        String encryptedText = cryptoOperationsController.encryptText(plainText);
        textArea.append("Szyfruję tekst : " + plainText + "\n");
        textArea.append("Zaszyfrowano wprowadzony tekst do pliku " + AppConfig.TEXT_ENCRYPTION_OUTPUT_FILE_PATH + "\n");
        textArea.append("Zaszyfrowany tekst : " + encryptedText + "\n");
        textArea.append("Odszyfrowywanie...\n");
        textArea.append("Odszyfrowany tekst : " + cryptoOperationsController.decryptText(encryptedText) + "\n");
    }

    private static void createBottomPanel() {
        //Creating the panel at bottom and adding components
        panel = new JPanel(); // the panel is not visible in output
        JLabel enterTextLabel = new JLabel("Wprowadz tekst");
        textField = new JTextField(30); // accepts upto 10 characters
        encryptBtn = new JButton("Zaszyfruj");
        JButton resetBtn = new JButton("Wyczyść");
        selectFileToEncryptBtn = new JButton("Zaszyfruj plik");
        selectFileToDecryptBtn = new JButton("Deszyfruj plik");
        panel.add(enterTextLabel);
        panel.add(enterTextLabel);
        panel.add(textField);
        panel.add(encryptBtn);
        panel.add(resetBtn);
        panel.add(selectFileToEncryptBtn);
        panel.add(selectFileToDecryptBtn);

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
                cryptoOperationsController.encryptFile(file);
                textArea.append("Zaszyfrowano plik " + file.getName() + " do pliku " + AppConfig.ENCRYPTED_FILE_PATH + "\n");

            }

        });

        selectFileToDecryptBtn.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                cryptoOperationsController.decryptFile(file);
                textArea.append("Odszyfrowano plik " + file.getName() + " do pliku " + AppConfig.DECRYPTED_FILE_PATH + "\n");

            }
        });

    }

    private static JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);
        return mb;
    }

    private static void handleFileEncrypting(TripleDESEncrypter tripleDESEncrypter) {
//        ClassLoader classLoader = Main.class.getClassLoader();
//        File plainTextFile = new File(classLoader.getResource("plainTextFile.txt").getFile());
//        File encryptedFile = new File(classLoader.getResource("encryptedFile.txt").getFile());
//        File decryptionResultFile = new File(classLoader.getResource("decryptedFile.txt").getFile());
//
//        String plainTextFilePath = getPathFromFile(plainTextFile);
//        String encryptedFilePath = getPathFromFile(encryptedFile);
//        String decryptionResultFilePath = getPathFromFile(decryptionResultFile);
//
//        tripleDESEncrypter.encryptFile(plainTextFilePath, encryptedFilePath);
//        tripleDESEncrypter.decryptFile(encryptedFilePath, decryptionResultFilePath);

        tripleDESEncrypter.encryptFile(AppConfig.PLAIN_TEXT_FILE_PATH, AppConfig.ENCRYPTED_FILE_PATH);
        tripleDESEncrypter.decryptFile(AppConfig.ENCRYPTED_FILE_PATH, AppConfig.DECRYPTED_FILE_PATH);


        System.out.println("Zaszyfrowano zawartość pliku testowego do pliku " + AppConfig.TEXT_ENCRYPTION_OUTPUT_FILE_PATH);
        System.out.println("Odszyfrowano zawartość do pliku " + AppConfig.DECRYPTED_FILE_PATH);
    }

    private static String getPathFromFile(File plainTextFile) {
        return plainTextFile.getAbsolutePath().replace("\\", "/");
    }

    private static void handleTextEncrypting(TripleDESEncrypter tripleDESEncrypter, Scanner scanner) {
        System.out.println("Wprowadz tekst do zaszyfrowania");
        String text = scanner.nextLine();
        String encrypted = tripleDESEncrypter.encrypt(text);
        String decrypted = tripleDESEncrypter.decrypt(encrypted);

        System.out.println("Wprowadzono: " + text);
        System.out.println("Po zaszyforowaniu:" + encrypted);
        System.out.println("Po odszyfrowaniu:" + decrypted);
        System.out.println("Szyfrogram zapisano w " + AppConfig.TEXT_ENCRYPTION_OUTPUT_FILE_PATH);
    }

}
