import java.util.concurrent.*;
import java.util.Scanner;
import java.util.Random;

public class AsyncSquareCalculator {

    // Метод, который имитирует задержку и возводит число в квадрат
    private static Integer calculateSquare(Integer input) {
        Random rand = new Random();
        int delay = rand.nextInt(5) + 1; // задержка от 1 до 5 секунд
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println("Calculation was interrupted");
            return null;
        }
        return input * input;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4); // создаем пул потоков с 4 потоками
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a number to calculate its square (or 'exit' to quit): ");
            String userInput = scanner.nextLine();

            // Если пользователь ввел "exit", прерываем цикл
            if (userInput.equalsIgnoreCase("exit")) {
                executor.shutdown();
                break;
            }

            try {
                // Преобразуем ввод в число
                int number = Integer.parseInt(userInput);

                // Создаем асинхронную задачу для расчета квадрата числа
                Future<Integer> futureResult = executor.submit(() -> calculateSquare(number));

                System.out.println("Processing your request...");

                // Асинхронно получаем результат, не блокируя ввод новых данных
                executor.submit(() -> {
                    try {
                        int result = futureResult.get();
                        System.out.println("Result: " + number + "^2 = " + result);
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println("An error occurred during the calculation: " + e.getMessage());
                    }
                });

            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number.");
            }
        }

        scanner.close();
        System.out.println("Program terminated.");
    }
}
