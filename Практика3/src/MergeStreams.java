import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;

public class MergeStreams {
    public static void main(String[] args) {
        Observable<Integer> firstStream = Observable.range(1, 1000)
                .map(i -> ThreadLocalRandom.current().nextInt(0, 10)); // Первый поток случайных цифр от 0 до 9

        Observable<Integer> secondStream = Observable.range(1, 1000)
                .map(i -> ThreadLocalRandom.current().nextInt(0, 10)); // Второй поток случайных цифр от 0 до 9

        // Объединение потоков последовательно
        Observable.concat(firstStream, secondStream)
                .subscribe(System.out::println); // Вывод результата
    }
}
