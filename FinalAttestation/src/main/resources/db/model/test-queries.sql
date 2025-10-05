--список заказов за последние 7 дней
SELECT 
    c.first_name || ' ' || c.last_name AS покупатель,
    p.description AS товар,
    o.order_date,
    o.quantity,
    s.name AS статус
FROM 
    orders o
JOIN 
    customers c ON o.customer_id = c.id
JOIN 
    products p ON o.product_id = p.id
JOIN 
    order_status s ON o.status_id = s.id
WHERE 
    o.order_date >= current_date - interval '7 days'
ORDER BY 
    o.order_date DESC;

--TOP 3 самых популярных товара
SELECT
    p.description AS товар,
    SUM(o.quantity) AS общий_заказанный_объем
FROM
    orders o
JOIN
    products p ON o.product_id = p.id
GROUP BY
    p.id, p.description
ORDER BY
    общий_заказанный_объем DESC
LIMIT 3;

--Общая выручка за последний месяц (по статусам заказа и по товарам)
SELECT 
    s.name AS статус_заказа,
    p.description AS товар,
    SUM(o.quantity * p.price) AS выручка
FROM
    orders o
JOIN
    products p ON o.product_id = p.id
JOIN
    order_status s ON o.status_id = s.id and s.name <> 'Отменен'
WHERE
    o.order_date >= date_trunc('month', CURRENT_DATE)
GROUP BY
    s.name, p.description
ORDER BY
    выручка DESC;


--Товары, которые не были заказаны за последние 30 дней
SELECT 
    p.description AS товар
FROM 
    products p
LEFT JOIN 
    orders o ON p.id = o.product_id AND o.order_date >= CURRENT_DATE - interval '30 days'
WHERE 
    o.id IS NULL;

--Список заказов в статусе “Новый”
SELECT
    o.id AS заказ_id,
    c.first_name || ' ' || c.last_name AS покупатель,
    p.description AS товар,
    o.order_date,
    o.quantity,
    s.name AS статус
FROM
    orders o
JOIN
    customers c ON o.customer_id = c.id
JOIN
    products p ON o.product_id = p.id
JOIN
    order_status s ON o.status_id = s.id
WHERE
    s.name = 'Новый'
ORDER BY
    o.order_date DESC;

--Поставка – добавление количества товаров
UPDATE products
SET quantity = quantity + 1
WHERE id in (select id from products where description = 'Кулон с бриллиантом') ;


--испраление ошибки в описании продукта
UPDATE products
SET description = 'Кроссовки Nike Air Max размер 38'
WHERE description = 'Кроссовки Nike Air Max размер 37';

--применение скидки в 10% на товар
UPDATE products
SET price = price - price*0.1
WHERE description LIKE 'Стол кухонный';

--удаление покупателей без заказов
DELETE FROM customers
WHERE id NOT IN (
    SELECT DISTINCT customer_id FROM orders
);
--удаление отмененных заказов
DELETE FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE name = 'Отменен');
