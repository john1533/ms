package com.org.fourtween.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by John on 2017/9/28.
 */

public class FileUtils {


    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        return readFile(file,charsetName);
    }

    public static StringBuilder readFile(File file, String charsetName) {
        if (file == null || !file.isFile()) {
            return null;
        }
        try {
            FileInputStream is = new FileInputStream(file);
            return readFile(is,charsetName);

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {

        }


    }


    public static StringBuilder readFile(FileInputStream inputStream, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = null;
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(inputStream, charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }




    public static boolean writeFile(String filePath, String content, boolean append) {
        File file = new File(filePath);
        return writeFile(file, content, append);

    }

    public static boolean writeFile(File file, String content, boolean append) {
        if (content==null || content.equals("")) {
            return false;
        }

        FileWriter fileWriter = null;
        try {

            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;

        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

}
