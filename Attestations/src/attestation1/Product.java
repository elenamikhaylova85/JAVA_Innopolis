package attestation1;

import java.util.Objects;

public class Product {
    private String name;
    private double price;

    //конструктор
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    //геттеры
    public double getPrice() {

        return price;
    }

    public String getName() {

        return name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' + ", price=" + price +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;

        return Double.compare(price, product.price) == 0 && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Double.hashCode(price);
        return result;
    }
}
