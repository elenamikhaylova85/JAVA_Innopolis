package HomeWork011Addition.test;

import HomeWork011Addition.model.Car;
import HomeWork011Addition.repository.CarsRepository;
import HomeWork011Addition.repository.CarsRepositoryImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Запрашиваем имя входного файла у пользователя
        System.out.print("Введите имя входного файла: ");
        String inputFileName = scanner.nextLine();

        // Запрашиваем имя выходного файла у пользователя
        System.out.print("Введите имя выходного файла: ");
        String outputFileName = scanner.nextLine();

        // Создаем экземпляр репозитория
        CarsRepository carsRepository = new CarsRepositoryImpl();
        // Читаем данные из файла
        List<Car> cars;
        try {
            cars = carsRepository.readCarsFromFile(inputFileName);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }
        // 1) Номера всех автомобилей, имеющих заданный цвет или нулевой пробег
        // Заданные переменные для поиска
        String colorToFind = "Black";
        int mileageToFind = 0;
        Set<String> numberAuto = carsRepository.getCarNumbersByColorOrMileage(cars, colorToFind, mileageToFind);
        // 2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
        double n = 700; // Минимальная цена в тыс.
        double m = 800; // Максимальная цена в тыс.
        long UniqCount = carsRepository.getUniqueModelsCountInRange(cars, n, m);
        //3) Вывести цвет автомобиля с минимальной стоимостью.
        String ColorMinCost= carsRepository.getColorOfCheapestCar(cars);
        //4) Среднюю стоимость искомой модели modelToFind
        String modelToFind = "Volvo";
        // String modelToFind = "Toyota";
        double AvgCost = carsRepository.getAveragePriceOfModel(cars,modelToFind);

        //вывод в файл
        // Запись результатов в файл согласно требованиям
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            bw.write("Автомобили в базе:");
            bw.newLine();
            bw.write("Number Model Color Mileage Cost");
            bw.newLine();
            for (Car c:cars) {
                bw.write(c.toString());
                bw.newLine();
            }
            bw.write("1)Номера автомобилей с заданным цветом или нулевым пробегом: " + numberAuto);
            bw.newLine();
            bw.write("2)Количество уникальных моделей в ценовом диапазоне от " + n + " до " + m + " тыс.: " + UniqCount);
            bw.newLine();
            bw.write("3)Цвет автомобиля с минимальной стоимостью: " + ColorMinCost);
            bw.newLine();
            bw.write("4)Средняя стоимость модели '" + modelToFind + "': " + AvgCost);
            bw.newLine();

            System.out.println("Результаты успешно записаны в файл: " + outputFileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
        scanner.close();
    }

}
