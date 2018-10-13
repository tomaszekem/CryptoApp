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
    private static CryptoOperationsController cryptoOperationsController = new CryptoOperationsController();
    private static final JFileChooser fileChooser = new JFileChooser();

    public static void main(String[] args) throws Exception {
        initView();

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

    private static void encryptTextAndPrintResult(String plainText) {
        String encryptedText = cryptoOperationsController.encryptText(plainText);
        textArea.append("Szyfruję tekst : " + plainText + "\n");
        textArea.append("Zaszyfrowano wprowadzony tekst do pliku " + AppConfig.getTextEncryptionOutputFilePath() + "\n");
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
        encryptFileToDatabaseBtn = new JButton("Zaszyfruj plik do bazy danych");
        decryptFileFromDatabaseBtn = new JButton("Odszyfruj plik z bazy danych");
        panel.add(enterTextLabel);
        panel.add(enterTextLabel);
        panel.add(textField);
        panel.add(encryptBtn);
        panel.add(resetBtn);
        panel.add(selectFileToEncryptBtn);
        panel.add(selectFileToDecryptBtn);
        panel.add(encryptFileToDatabaseBtn);
        panel.add(decryptFileFromDatabaseBtn);

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
                cryptoOperationsController.encryptFile(file);
                textArea.append("Zaszyfrowano plik " + file.getName() + " do pliku " + AppConfig.getEncryptedFilePath() + "\n");

            }

        });

        selectFileToDecryptBtn.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                cryptoOperationsController.decryptFile(file);
                textArea.append("Odszyfrowano plik " + file.getName() + " do pliku " + AppConfig.getDecryptedFilePath() + "\n");

            }
        });

        encryptFileToDatabaseBtn.addActionListener(a -> {
            int result = fileChooser.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    cryptoOperationsController.encryptFileToDatabase(file);
                    textArea.append("Zapisano zaszyfrowany plik " + file.getName() + " do bazy danych \n");
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
                    cryptoOperationsController.decryptFileFromDB(selectedFileName);
                    textArea.append("Odszyfrowany plik " + selectedFileName + " znajduje się w katalogu "
                    + AppConfig.getDecryptedFilesFromDbPath() + "\n");
                } catch (IOException e) {
                    textArea.append("Błąd podczas szyfrowania pliku do bazy danych \n");
                }
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

}
