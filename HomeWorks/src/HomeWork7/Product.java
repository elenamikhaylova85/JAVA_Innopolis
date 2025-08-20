package HomeWork7;
import java.util.Objects;

public class Product {
    private String name;//наименование продукта
    private double price;//стоимось продукта
    //конструтор
    public Product(String name, double price) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        } else if (name.length() < 3) {
            throw new IllegalArgumentException("Наименование продукта не может быть короче 3 символов");
        } else if (name.matches("\\d+")) {
            throw new IllegalArgumentException("Наименование продукта не может содержать только цифры");
        } else if (price <= 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательной или нулевой.");
        } else {
            this.name = name;
            this.price = price;
        }
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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

    @Override
    public String toString() {
        return name+" за "+price;
    }

}
