import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class CreateFile {
    public static void main(String[] args) throws IOException {
        byte[] data = new byte[1024 * 1024]; // 1 Мб
        try (FileOutputStream fos = new FileOutputStream("testFile.dat")) {
            for (int i = 0; i < 100; i++) {
                fos.write(data);
            }
        }
    }
}
