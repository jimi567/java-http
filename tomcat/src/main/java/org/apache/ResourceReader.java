package org.apache;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceReader {

    private static final String STATIC_RESOURCE_BASE_URL = "static";
    private static final String DEFAULT_RESOURCE_EXTENSION = ".html";

    public static boolean canRead(String path) {
        return toURL(path) != null;
    }

    public static String readFile(String path) throws IOException {
        File file = new File(toURL(path).getFile());
        Path filePath = file.toPath();
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    private static URL toURL(String path) {
        URL resource = ResourceReader.class.getClassLoader().getResource(STATIC_RESOURCE_BASE_URL + path);
        if (resource == null) {
            resource = ResourceReader.class.getClassLoader()
                    .getResource(STATIC_RESOURCE_BASE_URL + path + DEFAULT_RESOURCE_EXTENSION);
        }
        return resource;
    }
}
