package com.jfcore.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.common.io.Resources;

public class FileUtil {
    public static String CLASSPATH = FileUtil.class.getResource("/").getPath() + File.separator;

    public static List<String> readLines(String file) throws IOException {
        return FileUtils.readLines(new File(file));
    }

    public static List<String> readLinesFromResource(String resourceName) throws IllegalArgumentException, URISyntaxException, IOException {
        File file = new File(Resources.getResource(resourceName).toURI());
        return FileUtils.readLines(file);
    }

    public static String readFileToString(String file) throws IOException {
        return FileUtils.readFileToString(new File(file));
    }

    public static void writeStringToFile(String file, String data) throws IOException {
        FileUtils.writeStringToFile(new File(file), data);
    }
    
    public static void writeByteArrayToFile(String file, byte[] data) throws IOException {
        FileUtils.writeByteArrayToFile(new File(file), data);
    }

    public static void copyFile(String srcFile, String destFile) throws IOException {
        FileUtils.copyFile(new File(srcFile), new File(destFile));
    }
    
    public static String getName(String filename) {
        return FilenameUtils.getName(filename);
    }
    
    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(readFileToString(CLASSPATH + "mail.properties"));
    }

}
