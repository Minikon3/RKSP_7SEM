import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SequentialSum {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int[] array = loadArray("array.txt");

        long startTime = System.currentTimeMillis();
        long sum = 0;
        //запуск последовательного потока
        for (int num : array) {
            sum += num;
            try {
                Thread.sleep(1); // Задержка 1 мс
            } catch (InterruptedException e) { //если что-то не так
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Sequential Sum: " + sum);
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
}
