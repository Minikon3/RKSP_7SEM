import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class SensorSystem {

    public static void main(String[] args) {

        // Датчик температуры
        Observable<Integer> temperatureSensor = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> 15 + (int) (Math.random() * 16)) // Генерация случайной температуры от 15 до 30
                .doOnNext(temp -> System.out.println("Температура: " + temp + "°C"));

        // Датчик CO2
        Observable<Integer> co2Sensor = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> 30 + (int) (Math.random() * 71)) // Генерация случайного уровня CO2 от 30 до 100
                .doOnNext(co2 -> System.out.println("CO2: " + co2 + " ppm"));

        // Подписка на оба датчика и проверка сигнализации
        Observable.combineLatest(temperatureSensor, co2Sensor, (temp, co2) -> {
            if (temp > 25 && co2 > 70) {
                return "ALARM!!! Температура: " + temp + "°C, CO2: " + co2 + " ppm";
            } else if (temp > 25) {
                return "Внимание! Температура превышает норму: " + temp + "°C";
            } else if (co2 > 70) {
                return "Внимание! CO2 превышает норму: " + co2 + " ppm";
            }
            return "Все в норме.";
        }).subscribe(System.out::println);

        // Время запуска на 20 секунд
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
