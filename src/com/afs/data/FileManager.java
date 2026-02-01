package com.afs.data;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DATA_DIR = "data/";

    public static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static List<String> readFile(String filename) {
        ensureDataDirectory();
        List<String> lines = new ArrayList<>();
        File file = new File(DATA_DIR + filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeFile(String filename, List<String> data) {
        ensureDataDirectory();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + filename))) {
            for (String line : data) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String filename, String line) {
        ensureDataDirectory();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + filename, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
