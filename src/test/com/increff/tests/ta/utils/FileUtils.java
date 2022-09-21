package com.increff.tests.ta.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
  public static MultipartFile loadCSV(String filePath, String fileName) throws IOException {
     InputStream inputStream = ClassLoader.getSystemResource(filePath).openStream();
     return new MockMultipartFile(fileName, fileName, "text/csv", inputStream);
  }

  public static MultipartFile loadCsvWithoutExtension(String filePath, String fileName) throws IOException {
    InputStream inputStream = ClassLoader.getSystemResource(filePath).openStream();
    return new MockMultipartFile("fileName", "fileName", "text/csv", inputStream);
  }
}