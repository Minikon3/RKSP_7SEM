import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateArray {
    public static void main(String[] args) {
        int[] array = new int[10000];
        Random random = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }

        try (FileWriter writer = new FileWriter("array.txt")) {
            for (int num : array) {
                writer.write(num + "\n"); // Записываем каждое число в новой строке
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Array generated and saved to text file.");
    }
}
