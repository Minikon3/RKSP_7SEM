import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileChecksum {

    public static int calculateChecksum(String filePath) throws IOException {
        // Открываем файл для чтения
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192]; // Читаем файл кусочками по 8192 байта
            int bytesRead; // Сколько байт прочитали за один раз
            int checksum = 0; // Инициализируем контрольную сумму как 0

            // Читаем файл пока есть данные
            while ((bytesRead = fis.read(buffer)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);

                // Пока можем читать по 2 байта
                while (byteBuffer.remaining() > 1) {
                    // Получаем 2 байта и соединяем их в 16-битное число
                    int word = (byteBuffer.get() & 0xFF) << 8 | (byteBuffer.get() & 0xFF);
                    // Прибавляем к контрольной сумме и сохраняем только младшие 16 бит
                    checksum = (checksum + word) & 0xFFFF;
                }

                // Если остался один байт, обрабатываем его
                if (byteBuffer.remaining() == 1) {
                    int word = (byteBuffer.get() & 0xFF) << 8;
                    checksum = (checksum + word) & 0xFFFF;
                }
            }

            return checksum; // Возвращаем итоговую контрольную сумму
        }
    }

    public static void main(String[] args) {
        try {
            String filePath = "Titul_TIABD.docx"; // Путь к файлу
            int checksum = calculateChecksum(filePath);
            System.out.println("16-битная контрольная сумма: " + Integer.toHexString(checksum));
        } catch (IOException e) {
            e.printStackTrace(); // Обрабатываем ошибки чтения файла
        }
    }
}
