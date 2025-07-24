package ru.java.homework4;
import java.util.Scanner;
import java.util.Random;
public class App {
    public static void main (String[] args) {
        //Samsung экземпляр класса TV
        TV Samsung = new TV ("Samsung UE43DU8500UXRU",43);
        System.out.println("У телевизора " + Samsung.getModel() +" диагональ экрана " + Samsung.getDiagonal()+" дюйма(ов)");
        //Sony эксземпляр класса TV, модель вводится с экрана, диагональ выбирается рандомно
        Scanner scanner=new Scanner(System.in);
        System.out.println("Введите модель телевизора:");
        String model = scanner.nextLine();
        Random random=new Random();
        int diagonal=random.nextInt(19,101);
        TV Sony = new TV(model,diagonal);
        System.out.println("У телевизора " + Sony.getModel() +" диагональ экрана " + Sony.getDiagonal()+" дюйма(ов)");
    }
}
