package attestation1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class App {
    private static ArrayList<Person> AddPersons(String src) {        //функция ввод данных о покупателях
        ArrayList<Person> persons = new ArrayList<>(); //для объектов типа Person
        String[] personPairs = src.split(";"); //разделение на пары Имя=Сумма
        for (String pair : personPairs) {
            String[] parts = pair.split("=");
            String name = parts[0].trim();
            String money = parts[1].trim();
            if (parts.length != 2) {
                System.out.println("Неверный формат ввода. Пожалуйста, используйте формат 'Имя = Сумма");
                continue;
            } else if (name == null || name.isEmpty()) {
                System.out.println("Имя не может быть пустым ");
            } else if (name.length() < 3) {
                System.out.println("Имя не может быть короче 3 сиволов");
            } else if (Double.parseDouble(money) < 0) {
                System.out.println("Деньги не могут быть отрицательными");
            } else {
                Person person = new Person(name, Double.parseDouble(money));
                persons.add(person);
            }
        }
        return persons;
    }

    public static ArrayList<Product> AddProducts(String src) {//функция для ввода данных о продуктах
        ArrayList<Product> products = new ArrayList<>();
        String[] productPairs = src.split(";");
        for (String pair : productPairs) {
            String[] parts = pair.split("=");
            String name = parts[0].trim();
            String price = parts[1].trim();
            if (parts.length != 2) {
                System.out.println("Неверный формат ввода. Пожалуйста, используйте формат 'Наименование = Стоимость");
                continue;
            } else if (name == null || name.isEmpty()) {
                System.out.println("Наименование не может быть пустым ");
            } else if (Double.parseDouble(price) < 0) {
                System.out.println("Стоимость не может быть отрицательной");
            } else {
                Product product = new Product(name, Double.parseDouble(price));
                products.add(product);
            }
        }
        return products;
    }


    public static ArrayList<String> ShowReport(ArrayList<String> src) {
        //вывод отчета
        // Группируем продукты по покупателям
        //использовала HashMap, так как уже немного с ними знакома, все остальные решения были не очень
        Map<String, List<String>> groupedData = new HashMap<>(); // пара Имя, Набор продуктов
        ArrayList<String> out = new ArrayList<>();
        for (String entry : src) {
            String[] parts = entry.split("-");
            String personName = parts[0].trim();
            String productName = parts[1].trim();
            //Добавляем название продукта в список продуктов для соответствующего покупателя. Если покупатель ещё не добавлен в map, создаём новый список для него
            groupedData.computeIfAbsent(personName, k -> new ArrayList<>()).add(productName);
        }

        // Формируем строки в требуемом формате

        for (Map.Entry<String, List<String>> entry : groupedData.entrySet()) { //Проходим по каждой записи в map.
            String persName = entry.getKey();
            List<String> prods = entry.getValue();

            StringBuilder productsList = new StringBuilder(); //Создаём `StringBuilder` для формирования строки с продуктами.
            for (String product : prods) {
                productsList.append(product).append(", "); //Добавляем каждый продукт в строку, разделяя запятыми и пробелами.
            }

            // Удаляем последнюю запятую и пробел, если список не пустой
            if (productsList.length() > 0) {
                productsList.setLength(productsList.length() - 2);
            }
            out.add(persName + "-" + productsList.toString());
        }
        return out;
    }

    public static void main(String[] args) {
        ArrayList<String> sRep = new ArrayList<>(); //строки для вывода
        ArrayList<String> str = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        //ввод данных о покупателях
        System.out.println("Введите данные о покупателях: Имя=Сумма денег;");
        String inputPersons = scanner.nextLine();
        ArrayList<Person> persons = AddPersons(inputPersons);

        //ввод данных о продуктах аналогично данным о покупателях
        System.out.println("Введите данные о продуктах в формате: Наименование=Стоимость;");
        String inputProducts = scanner.nextLine();
        ArrayList<Product> products = AddProducts(inputProducts);

        //покупка продуктов
        System.out.println("Введите сведения о покупках в формате: Покупатель - продукт последовательно, для окончания введите END");
        while (true) {
            String inputPurchases = scanner.nextLine();
            if (inputPurchases.trim().equalsIgnoreCase("end")) { //если введен END
                break;
            } else { //совершаем покупку
                String[] parts = inputPurchases.split("-");
                String person_name = parts[0].trim();
                String product_name = parts[1].trim();
                for (Person person : persons) {
                    if (person.getName().equals(person_name)) {
                        for (Product product : products) {
                            if (product.getName().equals(product_name)) {
                                if (person.getMoney() - product.getPrice() < 0) { //считаем хватит ли у человека денег на данную покупку
                                    System.out.println(person.getName() + " не может позволить себе " + product.getName());
                                } else {
                                    double diff = person.getMoney() - product.getPrice();
                                    person.setMoney(diff); //устанавливаем новое значение Money после покупки
                                    System.out.println(person.getName() + " купил " + product.getName());
                                    str.add(person.getName() + "-" + product.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
       //формирование отчета о покупках
        sRep = App.ShowReport(str);
        for (Person per : persons) {
            boolean f = false;
            for (String s : sRep) {
                String[] subs = s.split("-");
                String subname = subs[0];
                String subprod = subs[1];
                if (per.getName().equals(subname)) {
                    f = true;
                    System.out.println(per.getName() + " - " + subprod);
                }
            }
            if (!f) {
                System.out.println(per.getName() + " Ничего не куплено");
                }

            }
        scanner.close();
    }
}