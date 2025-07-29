package ru.java.homework5;
import java.util.Scanner;
class Task1 {
    /*Для введенной с клавиатуры буквы английского алфавита
нужно вывести слева стоящую букву на стандартной клавиатуре. При этом
клавиатура замкнута, т.е. справа от буквы «p» стоит буква «a», а слева от "а"
буква "р", также соседними считаются буквы «l» и буква «z», а буква «m» с
буквой «q».*/

    public static void main() {
        String str = "qwertyuiopasdfghjklzxcvbnm";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите букву английского алфавита");
        String letter = scanner.nextLine();
        int index=str.indexOf(letter.toLowerCase());
        if ((letter.toLowerCase().equals("q"))) { //если введена q или Q
            System.out.println("m");
        }
        else { //во всех остальных случаях
            System.out.println(str.charAt(index-1));
        }
        scanner.close();
    }
}


