package org.antares;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static String currentPath;

    public synchronized static String getCurrentPath() throws URISyntaxException {
        if (currentPath != null) {
            return currentPath;
        }

        var codeSource = Application.class.getProtectionDomain().getCodeSource();
        File jarFile;

        var location = codeSource.getLocation();
        if (location != null) {
            String path = location.getPath();
            System.out.println("path[0] = " + location);
            if (path.contains("nested:/")) {
                path = path.substring(path.indexOf("/"));
                System.out.println("path[1] = " + path);
                path = path.substring(0, path.indexOf(".jar"));
                System.out.println("path[2] = " + path);
                path = path.substring(0, path.lastIndexOf("/"));
                System.out.println("path[3] = " + path);
                jarFile = new File(path);
            } else {
                jarFile = new File(location.toURI()).getParentFile();
            }
        }
        else {
            String path = Application.class.getResource(Application.class.getSimpleName() + ".class").getPath();
            String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
            jarFilePath = URLDecoder.decode(jarFilePath, StandardCharsets.UTF_8);
            jarFile = new File(jarFilePath).getParentFile();
        }
        currentPath = jarFile.getAbsolutePath() + File.separator;
        System.out.println("currentPath = " + currentPath);
        return currentPath;
    }
}
