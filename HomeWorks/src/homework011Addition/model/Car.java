package HomeWork011Addition.model;

public class Car {
    private String number;
    private String model;
    private String color;
    private int mileage;
    private double cost;

    //конструктор
    public Car(String number, String model, String color, int mileage, double cost) {
        this.number = number;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.cost = cost;
    }

    public String getNumber() {
        return number;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getMileage() {
        return mileage;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return number + ' ' + model + ' ' + color + ' ' + mileage + ' ' + cost;
    }
    // Статический метод для создания объекта Автомобиль из строки

    public static Car fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Неверный формат строки: " + data);
        }
        return new Car(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));

    }



}
