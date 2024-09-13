import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;

public class FirstFiveElements {
    public static void main(String[] args) {
        Observable.range(1, 10)
                .map(i -> ThreadLocalRandom.current().nextInt(0, 100)) // Генерация 10 случайных чисел от 0 до 99
                .take(5) // Выбираем только первые 5 чисел
                .subscribe(System.out::println); // Вывод результата
    }
}
