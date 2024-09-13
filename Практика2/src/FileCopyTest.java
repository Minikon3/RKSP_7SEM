import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

class FileCopyUsingStreams {
    public static void copyFile(String source, String destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}
class FileCopyUsingFileChannel {
    public static void copyFile(String source, String destination) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destinationChannel = new FileOutputStream(destination).getChannel()) {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }
}
class FileCopyUsingApacheCommonsIO {
    public static void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        FileUtils.copyFile(sourceFile, destinationFile);
    }
}
class FileCopyUsingFiles {
    public static void copyFile(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}

public class FileCopyTest {
    public static void main(String[] args) throws IOException {
        String source = "testFile.dat";
        String destination = "newFile.dat";

        // Вызов с использованием FileInputStream/FileOutputStream
        System.out.println("Вызов с использованием FileInputStream/FileOutputStream");
        FileCopyBenchmark.measurePerformance(FileCopyUsingStreams::copyFile, source, "newFile_InputStream.dat");

        // Вызов с использованием FileChannel
        System.out.println("Вызов с использованием FileChannel");
        FileCopyBenchmark.measurePerformance(FileCopyUsingFileChannel::copyFile, source, "newFile_FileChannel.dat");

        // Вызов с использованием Apache Commons IO
        System.out.println("Вызов с использованием Apache Commons IO");
        FileCopyBenchmark.measurePerformance(FileCopyUsingApacheCommonsIO::copyFile, source, "newFile_Apache.dat");

        // Вызов с использованием Files (NIO)
        System.out.println("Вызов с использованием Files (NIO)");
        FileCopyBenchmark.measurePerformance(FileCopyUsingFiles::copyFile, source, "newFile_NIO.dat");
    }
}
