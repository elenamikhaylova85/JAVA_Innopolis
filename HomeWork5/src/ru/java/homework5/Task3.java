package ru.java.homework5;

import java.util.Arrays;
import java.util.Scanner;

public class Task3 {
    /*Задача 3*. Задана строка, состоящая из букв английского алфавита,
разделенных одним пробелом. Необходимо каждую последовательность
символов упорядочить по возрастанию и вывести слова в нижнем регистре.
Входные данные: в единственной строке последовательность символов
представляющее два слова.
Выходные данные: упорядоченные по возрастанию буквы в нижнем
регистре.*/
    public static void main() {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Введите два слова через пробел:");
        String line= scanner.nextLine();
        String trimline = line.replaceAll(" ",""); //удаление пробелов
        char[] chars = trimline.toLowerCase().toCharArray();//создаем массив сиволов из введенной строки в нижнем регистре
        Arrays.sort(chars);//сортируем
        String sorted=new String(chars);
        System.out.println (chars);//результат
        scanner.close();
    }
    }

