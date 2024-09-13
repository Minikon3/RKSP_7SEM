import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

// Класс, представляющий файл
class FileTask {
    enum FileType { XML, JSON, XLS }

    private final FileType type;
    private final int size;

    public FileTask(FileType type, int size) {
        this.type = type;
        this.size = size;
    }

    public FileType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return type + " file of size " + size;
    }
}

// Генератор файлов
class FileGenerator implements Runnable {
    private final BlockingQueue<FileTask> queue;
    private final Random random = new Random();

    public FileGenerator(BlockingQueue<FileTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Генерация случайного файла
                FileTask.FileType[] types = FileTask.FileType.values();
                FileTask.FileType type = types[random.nextInt(types.length)];
                int size = random.nextInt(91) + 10; // Размер от 10 до 100

                // Симуляция времени генерации файла
                int delay = random.nextInt(901) + 100; // Задержка от 100 до 1000 мс
                Thread.sleep(delay);

                FileTask file = new FileTask(type, size);
                queue.put(file); // Добавление файла в очередь

                System.out.println("Generated: " + file);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("FileGenerator interrupted");
        }
    }
}

// Обработчик файлов
class FileProcessor implements Runnable {
    private final BlockingQueue<FileTask> queue;
    private final FileTask.FileType supportedType;

    public FileProcessor(BlockingQueue<FileTask> queue, FileTask.FileType supportedType) {
        this.queue = queue;
        this.supportedType = supportedType;
    }

    @Override
    public void run() {
        try {
            while (true) {
                FileTask file = queue.take(); // Извлечение файла из очереди
                if (file.getType() == supportedType) {
                    System.out.println("Processing " + file + " by " + supportedType + " processor");
                    Thread.sleep(file.getSize() * 7); // Симуляция времени обработки
                } else {
                    // Если тип файла не совпадает, возвращаем его обратно в очередь
                    queue.put(file);
                    Thread.sleep(100); // Короткая задержка, чтобы избежать постоянного цикла
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(supportedType + " processor interrupted");
        }
    }
}

public class FileProcessingSystem {
    public static void main(String[] args) {
        BlockingQueue<FileTask> queue = new LinkedBlockingQueue<>(5);

        // Создаем и запускаем генератор файлов
        FileGenerator generator = new FileGenerator(queue);
        Thread generatorThread = new Thread(generator);
        generatorThread.start();

        // Создаем и запускаем обработчики для каждого типа файла
        for (FileTask.FileType type : FileTask.FileType.values()) {
            FileProcessor processor = new FileProcessor(queue, type);
            Thread processorThread = new Thread(processor);
            processorThread.start();
        }
    }
}
