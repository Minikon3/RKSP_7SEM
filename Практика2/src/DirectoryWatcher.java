import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class DirectoryWatcher {

    // Метод для вычисления контрольной суммы 16 бит
    public static int calculateChecksum(String filePath) throws IOException {
        // Реализуем расчет контрольной суммы, как в прошлом задании
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            int checksum = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                while (byteBuffer.remaining() > 1) {
                    int word = (byteBuffer.get() & 0xFF) << 8 | (byteBuffer.get() & 0xFF);
                    checksum = (checksum + word) & 0xFFFF;
                }
                if (byteBuffer.remaining() == 1) {
                    int word = (byteBuffer.get() & 0xFF) << 8;
                    checksum = (checksum + word) & 0xFFFF;
                }
            }
            return checksum;
        }
    }

    // Метод для мониторинга каталога
    public static void watchDirectory(Path path) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        System.out.println("Наблюдение за каталогом: " + path);

        while (true) {
            WatchKey key = watchService.take(); // Ожидание события

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                Path fileName = ((WatchEvent<Path>) event).context();
                Path fullPath = path.resolve(fileName);

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("Файл создан: " + fileName);
                }

                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Файл изменен: " + fileName);
                    // Вызов метода для анализа изменений (список добавленных/удаленных строк)
                    analyzeFileChanges(fullPath.toFile());
                }

                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Файл удалён: " + fileName);

                    // Пытаемся получить размер файла и контрольную сумму перед удалением
                    File file = fullPath.toFile();
                    if (file.exists()) {
                        long fileSize = file.length();
                        System.out.println("Размер удалённого файла: " + fileSize + " байт");
                        try {
                            int checksum = calculateChecksum(fullPath.toString());
                            System.out.println("Контрольная сумма удалённого файла: " + Integer.toHexString(checksum));
                        } catch (IOException e) {
                            System.out.println("Не удалось рассчитать контрольную сумму: файл уже удален.");
                        }
                    } else {
                        System.out.println("Файл уже удален, невозможно получить размер или контрольную сумму.");
                    }
                }
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    // Метод для анализа изменений в файле
    public static void analyzeFileChanges(File file) throws IOException {
        System.out.println("Анализ изменений в файле: " + file.getName());
        // Пример упрощённого анализа (здесь должна быть реальная логика)
        List<String> oldContent = new ArrayList<>(); // Загружаем старую версию (можно хранить в памяти)
        List<String> newContent = Files.readAllLines(file.toPath()); // Читаем новую версию файла

        // Пример поиска добавленных строк
        for (String line : newContent) {
            if (!oldContent.contains(line)) {
                System.out.println("Добавлена строка: " + line);
            }
        }

        // Пример поиска удалённых строк
        for (String line : oldContent) {
            if (!newContent.contains(line)) {
                System.out.println("Удалена строка: " + line);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Path dir = Paths.get("DirectoryForWatching"); // Укажите путь к каталогу
        watchDirectory(dir);
    }
}
