package Homeworks07;

import java.util.ArrayList;
import java.util.Objects;

public class Person {
    private String name;
    private double money;
    private ArrayList<Product> bag_products;

    public Person(String name, double money) {
        this.name = name;
        this.money = money;
        this.bag_products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getBag_products() {
        return bag_products;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Person person)) return false;

        return Double.compare(money, person.money) == 0 && Objects.equals(name, person.name) && Objects.equals(bag_products, person.bag_products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Double.hashCode(money);
        result = 31 * result + Objects.hashCode(bag_products);
        return result;
    }

    public void buyProduct(Product product) {
        if (product.getPrice() <= money) {
            money -= product.getPrice();
            System.out.println(name + " купил " + product);
            bag_products.addLast(product);
        } else {
            System.out.println(name + " не может позволить себе " + product.getName());
        }
    }


    @Override
    public String toString() {
        return name +"-"+ bag_products.toString().replaceAll("^\\[|\\]$","");
    }
}
