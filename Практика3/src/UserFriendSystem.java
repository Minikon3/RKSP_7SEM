import io.reactivex.rxjava3.core.Observable;
import java.util.concurrent.ThreadLocalRandom;

class UserFriend {
    int userId;
    int friendId;

    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "UserFriend{userId=" + userId + ", friendId=" + friendId + "}";
    }
}

public class UserFriendSystem {

    // Генерация массива случайных UserFriend
    static UserFriend[] generateRandomUserFriends(int size) {
        UserFriend[] userFriends = new UserFriend[size];
        for (int i = 0; i < size; i++) {
            int userId = ThreadLocalRandom.current().nextInt(1, 101); // Генерация случайного userId от 1 до 100
            int friendId = ThreadLocalRandom.current().nextInt(1, 101); // Генерация случайного friendId от 1 до 100
            userFriends[i] = new UserFriend(userId, friendId);
        }
        return userFriends;
    }

    // Функция, возвращающая поток друзей по userId
    public static Observable<UserFriend> getFriends(int userId) {
        // Фильтрация по userId
        return Observable.fromArray(userFriendsArray)
                .filter(userFriend -> userFriend.userId == userId);
    }

    // Основной метод
    public static void main(String[] args) {
        // Генерация массива UserFriend
        int arraySize = 100;
        userFriendsArray = generateRandomUserFriends(arraySize);

        // Массив случайных userId
        Integer[] randomUserIds = {1, 5, 10, 20, 50};

        // Преобразование массива userId в поток объектов UserFriend через getFriends
        Observable.fromArray(randomUserIds)
                .flatMap(UserFriendSystem::getFriends) // Получение UserFriend через getFriends
                .subscribe(System.out::println); // Вывод результата
    }

    // Статический массив для работы с функцией getFriends
    private static UserFriend[] userFriendsArray;
}
