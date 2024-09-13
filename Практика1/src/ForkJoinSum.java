import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinSum {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int[] array = loadArray("array.txt");

        // Создание ForkJoinPool с количеством потоков по умолчанию
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();

        // Запуск задачи на вычисление суммы
        Long sum = forkJoinPool.invoke(new SumTask(array, 0, array.length));

        long endTime = System.currentTimeMillis();
        System.out.println("ForkJoin Sum: " + sum);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    private static int[] loadArray(String fileName) throws IOException {
        List<Integer> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Integer.parseInt(line.trim()));
            }
        }

        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    static class SumTask extends RecursiveTask<Long> {
        private final int[] array;
        private final int start, end;
        private static final int THRESHOLD = 1000; // Порог для разделения задачи
        //Конструктор задачи, где array массив для обработки, start - начальный инд, end - конечный
        SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        //метод считающий задачу, возвращает сумму элементов в диапазоне
        @Override
        protected Long compute() {
            // Если количество элементов меньше порога, обрабатываем последовательно
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                    try {
                        Thread.sleep(1); // Задержка 1 мс
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return sum;
            } else {
                // Иначе делим задачу на две подзадачи
                int middle = (start + end) / 2;
                SumTask leftTask = new SumTask(array, start, middle);
                SumTask rightTask = new SumTask(array, middle, end);
                // Запуск левой подзадачи асинхронно
                leftTask.fork();
                // Выполнение правой подзадачи синхронно
                long rightResult = rightTask.compute();
                // Ожидание завершения левой подзадачи и получение результата
                long leftResult = leftTask.join();
                return leftResult + rightResult;
            }
        }
    }
}
