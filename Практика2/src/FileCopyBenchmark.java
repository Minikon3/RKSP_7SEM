import java.io.IOException;

public class FileCopyBenchmark {

    public static void measurePerformance(FileCopyMethod fileCopyMethod, String source, String destination) throws IOException {
        // Очистка памяти
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Сборка мусора перед началом измерений

        // Замер времени и памяти до выполнения
        long startTime = System.nanoTime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        // Выполнение метода копирования
        fileCopyMethod.copy(source, destination);

        // Замер времени и памяти после выполнения
        long endTime = System.nanoTime();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        // Вывод результатов
        System.out.println("Время выполнения: " + (endTime - startTime) + " ns");
        System.out.println("Использование памяти: " + (endMemory - startMemory) + " байт");
    }
}
