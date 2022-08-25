package com.increff.ta.utils;

import org.junit.Assert;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static MockMultipartFile getFileFromResources(String path) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            return new MockMultipartFile(path, Files.readAllBytes(Paths.get(classloader.getResource(path).toURI())));
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
        return null;
    }
}
