package com.increff.ta.utils;

import java.net.URL;

public class FileUtils {
    public static URL getFileFromResources(String path){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResource(path);
    }
}
