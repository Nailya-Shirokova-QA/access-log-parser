import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Создаем сканер для чтения ввода из консоли
        Scanner scanner = new Scanner(System.in);

        // Получаем первое число
        System.out.println("Введите первое число:");
        int firstNumber = scanner.nextInt();

        // Получаем второе число
        System.out.println("Введите второе число:");
        int secondNumber = scanner.nextInt();

        // Вычисляем сумму
        int sum = firstNumber + secondNumber;

        // Вычисляем разность
        int difference = firstNumber - secondNumber;

        // Вычисляем произведение
        int product = firstNumber * secondNumber;

        // Вычисляем частное (используем double для точности)
        double quotient = (double) firstNumber / secondNumber;

        // Выводим результаты
        System.out.println("Сумма: " + sum);
        System.out.println("Разность: " + difference);
        System.out.println("Произведение: " + product);
        System.out.println("Частное: " + quotient);

        // Закрываем сканер (хорошая практика)
        scanner.close();
    }
}