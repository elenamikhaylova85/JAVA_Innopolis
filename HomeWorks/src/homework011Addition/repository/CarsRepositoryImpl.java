package HomeWork011Addition.repository;
import HomeWork011Addition.model.Car;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CarsRepositoryImpl implements CarsRepository {
    @Override
    // чтение входных данных из файла с использованием метода класса Car.fromString, который разбивает входные данные по полям
    public List<Car> readCarsFromFile(String fileName) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Car car = Car.fromString(line);
                    cars.add(car);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return cars;

    }
    //1) Номера всех автомобилей, имеющих заданный в переменной цвет colorToFind или нулевой пробег mileageToFind.
    @Override
    public Set<String> getCarNumbersByColorOrMileage (List<Car> cars, String colorToFind, int mileageToFind) {
        return cars.stream()
                .filter(car -> car.getColor().equals(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.toSet());
    }
   //2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
    @Override
    public long getUniqueModelsCountInRange (List<Car> cars, double n, double m) {
        return cars.stream()
                .filter(car -> car.getCost() >= n * 1000 && car.getCost() <= m * 1000)
                .map(Car::getModel)
                .distinct()
                .count();
    }
    //3) Вывести цвет автомобиля с минимальной стоимостью.
    @Override
    public String getColorOfCheapestCar (List<Car> cars) {
        return cars.stream()
                .min((car1, car2) -> Double.compare(car1.getCost(), car2.getCost()))
                .map(Car::getColor)
                .orElse(null);

    }
    //4) Средняя стоимость искомой модели modelToFind
    @Override
    public double getAveragePriceOfModel (List<Car> cars, String modelToFind) {
        return cars.stream()
                .filter(car -> car.getModel().equals(modelToFind))
                .mapToDouble(Car::getCost)
                .average()
                .orElse(0.0);
    }

}
