package HomeWork011Addition.repository;
import HomeWork011Addition.model.Car;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CarsRepository {
    List<Car> readCarsFromFile(String fileName) throws IOException;//чтение входных данных из файла
    Set<String> getCarNumbersByColorOrMileage (List<Car> cars, String colorToFind, int mileageToFind);//задание 1
    long getUniqueModelsCountInRange (List<Car> cars, double minPrice, double maxPrice);//задание 2
    String getColorOfCheapestCar (List<Car> cars);//задание 3
    double getAveragePriceOfModel (List<Car> cars, String modelToFind);//задание 4


}
