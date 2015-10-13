package ph.txtdis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Binary {

    public static InputStream inputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] toBytes(String name) {
        File file = new File(name);
        return toBytes(file);
    }

    public static Path toPath(String name) {
        File file = new File(name);
        return file.toPath();
    }

    public static byte[] toBytes(InputStream is) {
        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
