package HomeWork8;

import java.util.HashSet;
import java.util.Set;

/*Реализовать класс PowerfulSet, в котором должны быть следующие методы:
public <T> Set<T> intersection(Set<T> set1, Set<T> set2) – возвращает пересечение двух наборов.
Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {1, 2}
public <T> Set<T> union(Set<T> set1, Set<T> set2) – возвращает объединение двух наборов
Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {0, 1, 2, 3, 4}
public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) – возвращает элементы первого набора без тех, которые находятся также и во втором наборе.
Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {3}*/
public class Task3 {
    public static void main(String[] args) {
    PowerfulSet powerfulSet = new PowerfulSet();
    // Примеры множеств
    Set<Integer> set1 = new HashSet<>();
    set1.add(1);
    set1.add(2);
    set1.add(3);

    Set<Integer> set2 = new HashSet<>();
    set2.add(0);
    set2.add(1);
    set2.add(2);
    set2.add(4);

    // Пересечение
    Set<Integer> intersectionResult = powerfulSet.intersection(set1, set2);
    System.out.println("Пересечение: " + intersectionResult); // Ожидаемый результат: {1, 2}
    // Объединение
    Set<Integer> unionResult = powerfulSet.union(set1, set2);
    System.out.println("Объединение: " + unionResult); // Ожидаемый результат: {0, 1, 2, 3, 4}
    // Относительная разность
    Set<Integer> relativeComplementResult = powerfulSet.relativeComplement(set1, set2);
    System.out.println("Относительная разность: " + relativeComplementResult); // Ожидаемый результат: {3}
    }
}

