package HomeWork8;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*С консоли на вход подается две строки s и t. Необходимо вывести true, если одна строка является валидной анаграммой другой строки,
 и false – если это не так.
Анаграмма – это слово, или фраза, образованная путем перестановки букв другого слова или фразы, обычно с использованием всех исходных букв ровно один раз.
Для проверки:
● Бейсбол – бобслей
● Сосна – насос
● Лето – тело*/
public class Task2 {
    private static boolean isAnagram (String s,String t) {
        // Если строки имеют разную длину, они не могут быть анаграммами
        if (s.length() != t.length()) {
            return false;
        }
        StringBuilder str1 = new StringBuilder();
        s.chars()
                .map(Character::toLowerCase)
                .sorted()
                .forEach(str1::appendCodePoint);

        StringBuilder str2 = new StringBuilder();
        t.chars()
                .map(Character::toLowerCase)
                .sorted()
                .forEach(str2::appendCodePoint);
        return str1.toString().equals(str2.toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите первое слово");
        String word1 = scanner.nextLine();
        System.out.println("Введите второе слово");
        String word2 = scanner.nextLine();

        // Проверяем, являются ли строки анаграммами
        boolean result = isAnagram(word1, word2);
        // Выводим результат
        System.out.println(result);
    }
}
