package HomeWork11;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();
        try {
            Car car1 = Car.fromString("a123me|Mercedes|White|0|8300000");
            cars.add(car1);
            Car car2 = Car.fromString("b873of|Volga|Black|0|673000");
            cars.add(car2);
            Car car3 = Car.fromString("w487mn|Lexus|Grey|76000|900000");
            cars.add(car3);
            Car car4 = Car.fromString("p987hj|Volga|Red|610|704340");
            cars.add(car4);
            Car car5 = Car.fromString("c987ss|Toyota|White|254000|761000");
            cars.add(car5);
            Car car6 = Car.fromString("o983op|Toyota|Black|698000|740000");
            cars.add(car6);
            Car car7 = Car.fromString("p146op|BMW|White|271000|850000");
            cars.add(car7);
            Car car8 = Car.fromString("u893ii|Toyota|Purple|210900|440000");
            cars.add(car8);
            Car car9 = Car.fromString("l097df|Toyota|Black|108000|780000");
            cars.add(car9);
            Car car10 = Car.fromString("y876wd|Toyota|Black|160000|1000000");
            cars.add(car10);
            System.out.println("Автомобили в базе:");
            System.out.println("Number Model Color Mileage Cost");
            for (Car c:cars) {
                System.out.println(c);
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


        // 1) Номера всех автомобилей, имеющих заданный в переменной цвет colorToFind или нулевой пробег mileageToFind.
        String colorToFind = "Black";
        int mileageToFind = 0;
        List<String> numberAuto = cars.stream()
                .filter(car -> car.getColor().equals(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .toList();
        System.out.println("1)Номера автомобилей цвета " + colorToFind + " или пробегом = " + mileageToFind + ": " + numberAuto);

        //2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
        double n = 700; // Минимальная цена в тыс.
        double m = 800; // Максимальная цена в тыс.
        Long uniqCount = cars.stream()
                .filter(car -> car.getCost()>=n*1000 && car.getCost()<=m*1000)
                .map(Car::getModel)
                .distinct()
                .count();
        System.out.println("2)Количество уникальных моделей в ценовом диапазоне от " + n + " до " + m + " тыс.: " + uniqCount);

        //3) Вывести цвет автомобиля с минимальной стоимостью.
        Optional<String> ColorMinCost = cars.stream()
                .min(Comparator.comparingDouble(Car::getCost))
                .map(Car::getColor);
        System.out.println("3)Цвет автомобиля с минимальной стоимостью: " + ColorMinCost.orElse("не найден"));

        //4) Среднюю стоимость искомой модели modelToFind
        String modelToFind = "Volvo";
        // String modelToFind = "Toyota";
        Double AvgCost = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind))
                .mapToDouble(Car::getCost)
                .average()
                .orElse(0.00);
        System.out.println("4)Средняя стоимость автомобиля модели " + modelToFind +" : " + AvgCost);
    }
}
