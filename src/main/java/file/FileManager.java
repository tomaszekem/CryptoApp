package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.util.Objects.isNull;

public class FileManager {

    private static FileManager instance;

    private FileManager() {

    }

    public static FileManager getInstance() {
        if (isNull(instance)) {
            return new FileManager();
        }
        return instance;

    }

    public File loadFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    public void saveToFile(String text, File file) {
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
