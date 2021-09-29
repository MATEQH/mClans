package me.matthew.teams.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static File getFile(File parent, String fileName) {
        File file = new File(parent, fileName);
        return parent.exists() && file.exists() ? file : null;
    }

    public static File getOrCreateFile(File parent, String fileName) {
        File file = new File(parent, fileName);
        if (!parent.exists()) parent.mkdir();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean writeContent(File file, String content) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readContent(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            return stringBuilder.length() != 0 ? stringBuilder.toString() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
