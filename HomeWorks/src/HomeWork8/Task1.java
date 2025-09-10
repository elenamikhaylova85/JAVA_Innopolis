package HomeWork8;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*Реализовать метод, который на вход принимает ArrayList<T>,
 а возвращает набор уникальных элементов этого массива. Решить, используя коллекции*/
public class Task1 {
    public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
        // Создаем HashSet для хранения уникальных элементов
        Set<T> uniqueSet = new HashSet<>(list);
        return uniqueSet;
    }
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(4);

        Set<Integer> uniqueNumbers = getUniqueElements(numbers);
        System.out.println("Уникальные цифры: " + uniqueNumbers);

        ArrayList<String> words = new ArrayList<>();
        words.add("Петя");
        words.add("Вася");
        words.add("Петя");

        Set<String> uniqueWords = getUniqueElements(words);
        System.out.println("Уникальные имена:" + uniqueWords);


    }
}

