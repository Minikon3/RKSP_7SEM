import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadedSum {
    // Количество потоков, которые будут использоваться для обработки массива
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ExecutionException {
        int[] array = loadArray("array.txt");

        // Создание пула потоков с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        // Размер подмассива для каждого потока
        int chunkSize = array.length / THREAD_COUNT;
        // Массив Future для хранения результатов от каждого потока
        Future<Long>[] futures = new Future[THREAD_COUNT];

        long startTime = System.currentTimeMillis();

        // Разделение массива на части и запуск задач в пуле потоков
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int start = i * chunkSize;
            // Обработка последнего подмассива, который может быть больше, если длина массива не делится нацело
            final int end = (i == THREAD_COUNT - 1) ? array.length : start + chunkSize;
            // Создание задачи для вычисления суммы части массива
            futures[i] = executor.submit(() -> {
                long sum = 0;
                for (int j = start; j < end; j++) {
                    sum += array[j];
                    try {
                        Thread.sleep(1); // Задержка 1 мс
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return sum;
            });
        }

        // Сбор результатов от всех потоков и суммирование их
        long totalSum = 0;
        for (Future<Long> future : futures) {
            totalSum += future.get(); // Метод get() блокирует до получения результата
        }

        long endTime = System.currentTimeMillis();
        executor.shutdown();

        System.out.println("Threaded Sum: " + totalSum);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    private static int[] loadArray(String fileName) throws IOException {
        List<Integer> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Преобразование строки в целое число и добавление в список
                list.add(Integer.parseInt(line.trim()));
            }
        }
        // Преобразование списка в массив
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
