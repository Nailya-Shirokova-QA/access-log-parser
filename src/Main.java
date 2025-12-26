import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int fileCount = 0;

        while (true) {
            System.out.println("Введите путь к файлу (или 'exit' для выхода):");
            String path = scanner.nextLine();

            // Проверка на команду выхода
            if (path.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                break;
            }

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            // Проверка существования файла и что это не папка
            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Файл не существует.");
                }
                if (isDirectory) {
                    System.out.println("Указанный путь ведет к папке, а не к файлу.");
                }
                continue; // Продолжаем цикл
            }

            // Если файл существует и это именно файл
            System.out.println("Путь указан верно");
            fileCount++;
            System.out.println("Это файл номер " + fileCount);
        }

        scanner.close();
    }
}