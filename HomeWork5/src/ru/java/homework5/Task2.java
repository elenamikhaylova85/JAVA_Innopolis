package ru.java.homework5;
import java.util.Scanner;
class Task2 {
    /*Задана последовательность, состоящая только из символов ‘>’,
‘<’ и ‘-‘. Требуется найти количество стрел, которые спрятаны в этой
последовательности. Стрелы – это подстроки вида ‘>>-->’ и ‘<--<<’.*/
  public static void main() {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Введите произвольную последовательность, состоящую из симовлов:‘>’, ‘<’ и ‘-‘ ");
      String s = scanner.nextLine();
      int n=s.length();
      int count=0;
      for(int i=0; i<=n-5;i++) { // сравниваем каждые 5 символов из введенной строки с искомой
          String sub = s.substring(i,i+5);
          if(sub.equals(">>-->")||sub.equals("<--<<")) {
              count++;
          }
      }
      System.out.println("Всего стрелок: "+count);
      scanner.close();
  }
}

