package de.alexanderwodarz.code;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileCore {

    public static void createFile(String filePath) throws IOException {
        createDirectory(filePath);
        File file = new File(filePath);
        file.createNewFile();
    }

    public static void createDirectory(String path) {
        String[] splitted = path.split("/");
        if (splitted[splitted.length - 1].contains(".")) {
            path = path.substring(0, path.lastIndexOf("/"));
            splitted = path.split("/");
        }
        String current = "";
        for (String s : splitted) {
            if (s.contains("."))
                continue;
            current += s + "/";
            new File(current).mkdir();
        }
    }

    public static void appendFile(String file, String text) {
        appendFile(new File(file), text);
    }

    public static void appendFile(File file, String text) {
        appendFile(file, text, "UTF-8");
    }

    @SneakyThrows
    public static void appendFile(File file, String text, String charset) {
        if (!file.exists())
            createFile(file.getAbsolutePath());
        FileUtils.write(file, text, charset, true);
    }

    public static void writeFile(String file, String text) {
        writeFile(new File(file), text);
    }

    public static void writeFile(File file, String text) {
        writeFile(file, text, "UTF-8");
    }

    @SneakyThrows
    public static void writeFile(File file, String text, String charset) {
        if (!file.exists())
            createFile(file.getAbsolutePath());
        FileUtils.write(file, text, charset);
    }

    public static String readFile(String path) {
        return readFile(path, "UTF-8");
    }

    @SneakyThrows
    public static String readFile(String path, String charset) {
        File file = new File(path);
        if (!file.exists())
            return "";
        return FileUtils.readFileToString(new File(path), charset);
    }

}
