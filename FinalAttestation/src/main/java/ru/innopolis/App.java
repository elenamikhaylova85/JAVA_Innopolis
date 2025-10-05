package ru.innopolis;

import org.flywaydb.core.Flyway;
import java.sql.*;
import java.util.Properties;


public class App {
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;
    private static final String FLYWAY_LOC;
    static {
        Properties props = new Properties();
        try {
            props.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");
            FLYWAY_LOC = props.getProperty("flyway.locations");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки параметров из файла application.properties", e);
        }
    }
    public static void main(String[] args) {
        Connection connection = null;
        try {
            //Подключение к БД
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Подключение к БД произведено успешно.");
            // Запуск миграций Flyway
             runFlywayMigrations();
            // CRUD-операции
            // Начало транзакции
            connection.setAutoCommit(false);
            //1. Вставка нового товара и покупателя (PreparedStatement).
            insertProduct(connection,"Тушь для ресниц Clarins",2500,3,"Красота и уход");
            insertCustomer(connection,"Test_user","Test","89012345678",null);
            //2. Создание заказа для покупателя.
            createOrderForCustomer(connection,"Елена","Михайлова","89012345678","Тушь для ресниц Clarins",2);
            //3. Чтение и вывод последних 5 заказов с JOIN на товары и покупателей.
            printLastFiveOrders(connection);
            //4.Обновление цены товара и количества на складе.
            updateProductPriceAndQuantity(connection,"Тушь для ресниц Clarins",1000,1);
            //5.Удаление тестовых записей.
            deleteTestRecords(connection);
            // Фиксация транзакции
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Произведен откат изменений");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Не удалось закрыть соединение: " + e.getMessage());
                }
            }
        }
    }
    //запуск миграций
    private static void runFlywayMigrations() {
        Flyway flyway = Flyway.configure()
                .dataSource(DB_URL, DB_USER, DB_PASSWORD)
                .locations(FLYWAY_LOC)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        System.out.println("Flyway миграции выполнены успешно.");
    }
    //добавление нового товара
    private static void insertProduct(Connection connection, String description, double price, int quantity, String category) throws SQLException {
        String sql = "INSERT INTO products (description, price, quantity, category) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement = connection.prepareStatement(sql);
        statement.setString(1, description);
        statement.setDouble(2, price);
        statement.setInt(3, quantity);
        statement.setString(4, category);
        statement.executeUpdate();
        System.out.println("Товар успешно добавлен.");
    }
    //добавление нового покупателя
    private static void insertCustomer(Connection connection, String firstName, String lastName, String phone, String email) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement = connection.prepareStatement(sql);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, phone);
        statement.setString(4, email);
        statement.executeUpdate();
        System.out.println("Покупатель успешно добавлен.");
    }
    //добавление заказа по имени покупателя
    private static void createOrderForCustomer(Connection connection,String firstName, String lastName, String phone,String product,int quantity) throws SQLException {
        //получаем id покупателя в табличке customers, если его нет заводим нового
        String customerSql = "SELECT id FROM customers WHERE first_name = ? AND last_name = ?";
        PreparedStatement customerStatement = connection.prepareStatement(customerSql);
        customerStatement.setString(1, firstName);
        customerStatement.setString(2, lastName);
        ResultSet customerResult = customerStatement.executeQuery();
        int customerId = -1;
        if (customerResult.next()) {
            customerId = customerResult.getInt("id");
        } else {
            insertCustomer(connection,firstName,lastName,phone,null);
            customerResult = customerStatement.executeQuery();
            if (customerResult.next()) {
                customerId = customerResult.getInt("id");
            }
        }
        //получаем id продукта в таблице products
        String productSql = "SELECT id FROM products WHERE description = ?";
        PreparedStatement productStatement = connection.prepareStatement(productSql);
        productStatement.setString(1, product);
        ResultSet productResult = productStatement.executeQuery();
        int productId = -1;
        if (productResult.next()) {
            productId = productResult.getInt("id");
        }
        //получаем id статуса "Новый" в таблице order_status
        String statusSql = "SELECT id FROM order_status WHERE name = ?";
        PreparedStatement statusStatement = connection.prepareStatement(statusSql);
        statusStatement.setString(1, "Новый");
        ResultSet statusResult = statusStatement.executeQuery();
        int statusId = -1;
        if (statusResult.next()) {
            statusId = statusResult.getInt("id");
        }
        //создаем новый заказ
        String orderSql = "INSERT INTO orders (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)";
        PreparedStatement orderStatement = connection.prepareStatement(orderSql);
        orderStatement.setInt(1, productId);
        orderStatement.setInt(2, customerId);
        orderStatement.setInt(3, quantity);
        orderStatement.setInt(4, statusId);

        int rowsInserted = orderStatement.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Заказ создан");
        }
    }

    //вывести 5 последних заказов с информацией о товарах и покупателях
    public static void printLastFiveOrders(Connection connection) throws SQLException {
        System.out.println("5 последних заказов:");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT o.id AS order_id, p.description AS product_description," +
                " c.first_name AS customer_first_name, c.last_name AS customer_last_name," +
                " o.order_date AS order_date," +
                " os.name AS order_status FROM orders o" +
                " JOIN products p ON o.product_id = p.id" +
                " JOIN customers c ON o.customer_id = c.id" +
                " JOIN order_status os ON o.status_id = os.id" +
                " ORDER BY o.order_date DESC LIMIT 5";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("Заказ ID: " + resultSet.getInt("order_id"));
            System.out.println("Описание товара: " + resultSet.getString("product_description"));
            System.out.println("Имя покупателя: " + resultSet.getString("customer_first_name"));
            System.out.println("Фамилия покупателя: " + resultSet.getString("customer_last_name"));
            System.out.println("Дата заказа: " + resultSet.getTimestamp("order_date"));
            System.out.println("Статус заказа: " + resultSet.getString("order_status"));
            System.out.println("-----------------------------");
        }
    }
    private static void updateProductPriceAndQuantity(Connection connection, String product, double newPrice, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET price = ?, quantity = ? WHERE description = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement = connection.prepareStatement(sql);
        statement.setDouble(1, newPrice);
        statement.setInt(2, newQuantity);
        statement.setString(3, product);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Цена и количество товара успешно обновлены.");
        } else {
            System.out.println("Товар не найден.");
        }
    }

    private static void deleteTestRecords(Connection connection) throws SQLException {
        String deleteProductSql = "DELETE FROM products WHERE description LIKE 'Test%'";
        String deleteCustomerSql = "DELETE FROM customers WHERE first_name LIKE 'Test%' AND last_name LIKE 'Test%'";
        Statement statement = connection.createStatement();
        int productsDeleted = statement.executeUpdate(deleteProductSql);
        int customersDeleted = statement.executeUpdate(deleteCustomerSql);
        if (productsDeleted > 0 || customersDeleted > 0) {
            System.out.println("Тестовые записи успешно удалены.");
        } else {
            System.out.println("Тестовых записей не найдено.");
        }
        statement.close();
    }
}






