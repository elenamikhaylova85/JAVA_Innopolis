package HomeWork7;
import java.time.LocalDate;
public class DiscountProduct extends Product {
    private LocalDate end_date; // дата окончания действия скидки

    //конструктор
    public DiscountProduct(String name, double price, LocalDate end_date) {
        super(name,price);
        this.end_date = end_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }


}
