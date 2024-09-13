import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    public static void main(String[] args) {
        // Указываем путь к файлу file.txt
        Path filePath = Paths.get("file.txt");

        try {
            // Читаем все строки файла
            List<String> lines = Files.readAllLines(filePath);

            // Выводим содержимое файла на экран
            for (String line : lines) {
                System.out.println(line);
            }

        } catch (IOException e) {
            // Если произошла ошибка при чтении файла
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
