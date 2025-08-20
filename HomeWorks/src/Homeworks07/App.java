package Homeworks07;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;

public class App {
    private static ArrayList<Person> AddPersons(String src) {        //функция ввод данных о покупателях
        ArrayList<Person> persons = new ArrayList<>(); //для объектов типа Person
        String[] personPairs = src.split(";"); //разделение на пары Имя=Сумма
        for (String pair : personPairs) {
            String[] parts = pair.split("=");
            String name = parts[0].trim();
            String money = parts[1].trim();
            if (parts.length != 2) {
                throw new IllegalArgumentException ("Неверный формат ввода. Пожалуйста, используйте формат 'Имя = Сумма");
            } else if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Имя не может быть пустым");
            } else if (Double.parseDouble(money) < 0) {
                throw new IllegalArgumentException("Деньги не могут быть отрицательными");
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
            if (parts.length != 2) {
                throw new IllegalArgumentException("Неверный формат ввода. Пожалуйста, используйте формат 'Наименование = Цена");
            } else if (parts[0].trim().isEmpty()) {
                throw new IllegalArgumentException("Название продукта не может быть пустым.");
            } else if (parts[0].trim().length() < 3) {
                throw new IllegalArgumentException("Наименование продукта не может быть короче 3 символов");
            } else if (parts[0].trim().matches("\\d+")) {
                throw new IllegalArgumentException("Наименование продукта не может содержать только цифры");
            } else if (Double.parseDouble(parts[1].trim()) <= 0) {
                throw new IllegalArgumentException("Цена продукта не может быть нулевой или отрицательной");
            } else {
                Product product = new Product(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                products.add(product);
            }
        }
        return products;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //ввод данных о покупателях
        System.out.println("Введите данные о покупателях: Имя Отчество=Сумма денег; Отчество - необязательное поле");
        String inputPersons = scanner.nextLine();
        ArrayList<Person> persons = AddPersons(inputPersons);

        //ввод данных о продуктах
        System.out.println("Введите данные о продуктах в формате: Наименование=Цена;");
        String inputProducts = scanner.nextLine();
        ArrayList<Product> products = AddProducts(inputProducts);

        //ввод данных о продуктах со скидкой
        // сразу считаем цену со скидкой  и заводим список скидочных продуктов
        System.out.println("Ввести данные о скидках на продукты?ДА/НЕТ");
        String answer = scanner.nextLine();
        ArrayList<DiscountProduct> ArrDiscountProducts = new ArrayList<>();
        if (!answer.trim().equalsIgnoreCase("нет")) { //вводим данные о скидках
            System.out.println("Данные о скидках вносятся в формате: Размер скидки(в процентах) до Дата действия скидки в формате DD.MM.YYYY,если скидка на продукт не предусмотрена, введите 0");
            for (Product product : products) {
                System.out.println(product.getName());
                String inputDiscount = scanner.nextLine();
                if (inputDiscount.trim().isEmpty() || inputDiscount.trim().equals("0")) {
                    System.out.println("Скидка на продукт не предусмотрена");
                } else {
                    String[] productParts = inputDiscount.split("до");
                    if (productParts.length != 2) {
                        throw new IllegalArgumentException("Неверный формат ввода. Пожалуйста, используйте 'Размер скидки(в процентах) до Дата действия скидки в формате DD.MM.YYYY'");
                    } else if (Double.parseDouble(productParts[0].trim()) <= 0) {
                        throw new IllegalArgumentException("Скидка не может быть равно 0 или отрицательной");
                    } else if (Double.parseDouble(productParts[0].trim()) >= 100) {
                        throw new IllegalArgumentException("Скидка не может быть равна 100% или больше 100%");
                    } else {
                        Double new_price = product.getPrice()-product.getPrice() * Double.parseDouble(productParts[0].trim()) / 100;
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate date = LocalDate.parse(productParts[1].trim(),format);
                        DiscountProduct discountProduct = new DiscountProduct(product.getName(), new_price, date);
                        ArrDiscountProducts.add(discountProduct);
                    }
                }
            }
        }
        //покупка продуктов
        System.out.println("Введите сведения о покупках в формате: Покупатель Продукт последовательно, для окончания введите END");
        while (true) {
            String PersonName = new String();
            String ProductName = new String();
            String inputPurchases = scanner.nextLine();
            if (inputPurchases.trim().equalsIgnoreCase("end")) { //если введен END выходим из программы
                break;
            } else { //совершаем покупки
                String[] parts = inputPurchases.split("-");
                if (parts.length != 2) {
                    System.out.println("Неверный формат ввода. Пожалуйста, используйте формат 'Имя покупателя = Наименование продукта");
                } else if (parts[0].isEmpty()) {
                    System.out.println("Покупатель не найден");
                } else if (parts[1].isEmpty()) {
                    System.out.println("Наименование продукта не может быть пустым");
                } else {
                    PersonName = parts[0].trim();
                    ProductName = parts[1].trim();
                }
                boolean flag; //флаг покупки продукта со скидкой
                for (Person person : persons) {
                    //цикл по скидочным продуктам
                    //если есть продукт со скидкой, и дата действия скидки нас устраивает, то покупаем его
                    //если нет, то перебираем продукты без скидки
                    if (person.getName().equals(PersonName.trim())) {
                        for (Product product : products) {
                            flag = false;
                            if (product.getName().equals(ProductName.trim())) {
                                 for (DiscountProduct discountProduct : ArrDiscountProducts) {
                                     if (discountProduct.getName().equals(product.getName()) && discountProduct.getEnd_date().isAfter(LocalDate.now())) {
                                         person.buyProduct(discountProduct);
                                         flag=true;
                                     }
                                 }
                                 if (!flag) {
                                     person.buyProduct(product);
                                 }
                            }
                        }
                    }
                }
            }
        }
        for (Person person : persons) {
            if (person.getBag_products().isEmpty()) {
                System.out.println(person.getName() + " ничего не куплено");
            } else {
                System.out.println(person);
            }
        }
    }
}

