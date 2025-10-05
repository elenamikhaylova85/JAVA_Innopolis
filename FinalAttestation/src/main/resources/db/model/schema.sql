-- Создаем таблицу products
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    category VARCHAR(80) NOT NULL DEFAULT 'Прочие'
);

COMMENT ON TABLE products IS 'Таблица товаров, содержащая описание, цену, количество и категорию';
COMMENT ON COLUMN products.id IS 'Уникальный идентификатор продукта';
COMMENT ON COLUMN products.description IS 'Описание товара';
COMMENT ON COLUMN products.price IS 'Стоимость товара (от 0)';
COMMENT ON COLUMN products.quantity IS 'Количество на складе (от 0)';
COMMENT ON COLUMN products.category IS 'Категория товара';

-- Создаем таблицу customers
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100)
);

COMMENT ON TABLE customers IS 'Таблица клиентов с контактной информацией';
COMMENT ON COLUMN customers.id IS 'Уникальный идентификатор клиента';
COMMENT ON COLUMN customers.first_name IS 'Имя клиента';
COMMENT ON COLUMN customers.last_name IS 'Фамилия клиента';
COMMENT ON COLUMN customers.phone IS 'Телефон клиента';
COMMENT ON COLUMN customers.email IS 'Email клиента';

-- Таблица статусов заказов
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

COMMENT ON TABLE order_status IS 'Справочник статусов заказа';
COMMENT ON COLUMN order_status.id IS 'Уникальный идентификатор статуса';
COMMENT ON COLUMN order_status.name IS 'Название статуса';

-- Таблица заказов
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    customer_id INTEGER NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_date TIMESTAMP NOT NULL DEFAULT NOW(),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status_id INTEGER NOT NULL REFERENCES order_status(id)
);

COMMENT ON TABLE orders IS 'Таблица заказов, связывает товары с клиентами и статусами';
COMMENT ON COLUMN orders.id IS 'Уникальный идентификатор заказа';
COMMENT ON COLUMN orders.product_id IS 'ID продукта, связанного с заказом';
COMMENT ON COLUMN orders.customer_id IS 'ID клиента, сделавшего заказ';
COMMENT ON COLUMN orders.order_date IS 'Дата заказа';
COMMENT ON COLUMN orders.quantity IS 'Количество заказанного товара (больше 0)';
COMMENT ON COLUMN orders.status_id IS 'Статус заказа';

-- Создаем индексы по внешним ключам и дате заказа
CREATE INDEX IF NOT EXISTS idx_order_product_id ON orders (product_id);
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON orders (customer_id);
CREATE INDEX IF NOT EXISTS idx_order_status_id ON orders (status_id);
CREATE INDEX IF NOT EXISTS idx_order_order_date ON orders (order_date);