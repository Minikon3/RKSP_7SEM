import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;

public class FilterStream {
    public static void main(String[] args) {
        Observable.range(1, 1000)
                .map(i -> ThreadLocalRandom.current().nextInt(0, 1001)) // Генерация случайного числа от 0 до 1000
                .filter(number -> number > 500) // Фильтрация чисел > 500
                .subscribe(System.out::println); // Вывод результата
    }
}
