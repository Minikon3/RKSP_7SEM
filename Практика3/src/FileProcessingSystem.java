import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;

// Класс Файлов
class File {
    String type; // Тип файла (например, XML, JSON, XLS)
    int size;    // Размер файла

    // Конструктор файла
    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }

    // Метод для получения типа файла
    public String getType() {
        return type;
    }

    // Метод для получения размера файла
    public int getSize() {
        return size;
    }

    // Преобразование объекта в строку
    @Override
    public String toString() {
        return "Файл{" + "тип='" + type + '\'' + ", размер=" + size + '}';
    }
}

// Генератор файлов
class FileGenerator {
    private final String[] fileTypes = {"XML", "JSON", "XLS"}; // Возможные типы файлов
    private final Random random = new Random();

    // Генерация файлов с задержкой
    public Observable<File> generateFiles() {
        return Observable.interval(random.nextInt(1000) + 100, TimeUnit.MILLISECONDS)
                .map(tick -> new File(fileTypes[random.nextInt(fileTypes.length)], random.nextInt(91) + 10))
                .subscribeOn(Schedulers.io()); // Выполняем генерацию в асинхронном потоке
    }
}

// Очередь файлов с ограниченной вместимостью
class FileQueue {
    private final BlockingQueue<File> queue = new LinkedBlockingQueue<>(5); // Очередь на 5 файлов

    // Метод для добавления файла в очередь
    public void addToQueue(File file) throws InterruptedException {
        queue.put(file); // Блокируем очередь, если она заполнена
        System.out.println("Добавлен в очередь: " + file);
    }

    // Метод для извлечения файла из очереди
    public File getFromQueue() throws InterruptedException {
        return queue.take(); // Извлекаем файл, если он есть
    }
}

// Обработчик файлов
class FileProcessor {
    private final String fileType; // Тип файла, который этот обработчик может обработать

    public FileProcessor(String fileType) {
        this.fileType = fileType;
    }

    // Метод для обработки файлов
    public void process(File file) {
        if (file.getType().equals(fileType)) { // Обрабатываем только файлы нужного типа
            try {
                int processingTime = file.getSize() * 7; // Время обработки пропорционально размеру файла
                System.out.println("Обработка " + file + " (время обработки: " + processingTime + "мс)");
                Thread.sleep(processingTime); // Задержка обработки
                System.out.println("Файл обработан: " + file);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Основной класс программы
public class FileProcessingSystem {
    public static void main(String[] args) throws InterruptedException {
        // Создание объектов: генератора файлов, очереди и обработчиков
        FileGenerator generator = new FileGenerator();
        FileQueue queue = new FileQueue();
        FileProcessor xmlProcessor = new FileProcessor("XML");
        FileProcessor jsonProcessor = new FileProcessor("JSON");
        FileProcessor xlsProcessor = new FileProcessor("XLS");

        // Генерация файлов и добавление их в очередь
        generator.generateFiles()
                .observeOn(Schedulers.io()) // Наблюдаем в другом потоке
                .subscribe(file -> {
                    try {
                        queue.addToQueue(file);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        // Обработка файлов из очереди
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .subscribe(tick -> {
                    try {
                        File file = queue.getFromQueue();
                        // Передаём файл в обработчики
                        xmlProcessor.process(file);
                        jsonProcessor.process(file);
                        xlsProcessor.process(file);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        // Блокируем основной поток на 10 секунд, чтобы программа успела обработать файлы
        Thread.sleep(10000);
    }
}
