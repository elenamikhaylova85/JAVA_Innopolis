--������ ������� �� ��������� 7 ����
SELECT 
    c.first_name || ' ' || c.last_name AS ����������,
    p.description AS �����,
    o.order_date,
    o.quantity,
    s.name AS ������
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

--TOP 3 ����� ���������� ������
SELECT
    p.description AS �����,
    SUM(o.quantity) AS �����_����������_�����
FROM
    orders o
JOIN
    products p ON o.product_id = p.id
GROUP BY
    p.id, p.description
ORDER BY
    �����_����������_����� DESC
LIMIT 3;

--����� ������� �� ��������� ����� (�� �������� ������ � �� �������)
SELECT 
    s.name AS ������_������,
    p.description AS �����,
    SUM(o.quantity * p.price) AS �������
FROM
    orders o
JOIN
    products p ON o.product_id = p.id
JOIN
    order_status s ON o.status_id = s.id and s.name <> '�������'
WHERE
    o.order_date >= date_trunc('month', CURRENT_DATE)
GROUP BY
    s.name, p.description
ORDER BY
    ������� DESC;


--������, ������� �� ���� �������� �� ��������� 30 ����
SELECT 
    p.description AS �����
FROM 
    products p
LEFT JOIN 
    orders o ON p.id = o.product_id AND o.order_date >= CURRENT_DATE - interval '30 days'
WHERE 
    o.id IS NULL;

--������ ������� � ������� ������
SELECT
    o.id AS �����_id,
    c.first_name || ' ' || c.last_name AS ����������,
    p.description AS �����,
    o.order_date,
    o.quantity,
    s.name AS ������
FROM
    orders o
JOIN
    customers c ON o.customer_id = c.id
JOIN
    products p ON o.product_id = p.id
JOIN
    order_status s ON o.status_id = s.id
WHERE
    s.name = '�����'
ORDER BY
    o.order_date DESC;

--�������� � ���������� ���������� �������
UPDATE products
SET quantity = quantity + 1
WHERE id in (select id from products where description = '����� � �����������') ;


--���������� ������ � �������� ��������
UPDATE products
SET description = '��������� Nike Air Max ������ 38'
WHERE description = '��������� Nike Air Max ������ 37';

--���������� ������ � 10% �� �����
UPDATE products
SET price = price - price*0.1
WHERE description LIKE '���� ��������';

--�������� ����������� ��� �������
DELETE FROM customers
WHERE id NOT IN (
    SELECT DISTINCT customer_id FROM orders
);
--�������� ���������� �������
DELETE FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE name = '�������');
