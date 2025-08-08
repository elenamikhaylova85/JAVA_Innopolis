package attestation1;


import java.util.ArrayList;
import java.util.Objects;

public class Person {
    private String name;
    private double money;
    private ArrayList<Product> products;
    //конструктор
    public Person(String name, double money) {
        this.name = name;
        this.money = money;
    }

    //геттеры и сеттеры
    public void setMoney(double money) {
         this.money = money;
     }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", products=" + products +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Person person)) return false;

        return Double.compare(money, person.money) == 0 && Objects.equals(name, person.name) && Objects.equals(products, person.products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Double.hashCode(money);
        result = 31 * result + Objects.hashCode(products);
        return result;
    }
}
