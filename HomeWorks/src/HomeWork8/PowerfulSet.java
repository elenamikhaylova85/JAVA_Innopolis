package HomeWork8;

import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {
    // Метод для нахождения пересечения двух множеств
    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> resultSet = new HashSet<>(set1);
        resultSet.retainAll(set2);
        return resultSet;
    }

    // Метод для нахождения объединения двух множеств
    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> resultSet = new HashSet<>(set1);
        resultSet.addAll(set2);
        return resultSet;
    }

    // Метод для нахождения относительной разности (элементы первого множества, которых нет во втором)
    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> resultSet = new HashSet<>(set1);
        resultSet.removeAll(set2);
        return resultSet;
    }
}
