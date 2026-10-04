INSERT INTO customers (customer_name, email, phone) VALUES
('Rakshitha', 'rakshitha@gmail.com', '9876543210'),
('Arun', 'arun@gmail.com', '9876543211'),
('Anu', 'anu@gmail.com', '9876543212'),
('Rashmi', 'rashmi@gmail.com', '9876543213'),
('Chandana', 'chandana@gmail.com', '9876543214'),
('Karthik', 'karthik@gmail.com', '9876543215');

INSERT INTO menu (item_name, price) VALUES
('Chicken Biryani', 180.00),
('Veg Biryani', 140.00),
('Masala Dosa', 80.00),
('Paneer Butter Masala', 160.00),
('Chicken Fried Rice', 150.00),
('Coke', 40.00);

INSERT INTO orders (customer_id, order_date, total_amount, payment_method) VALUES
(1, '2026-10-01', 360.00, 'Prepaid'),
(2, '2026-10-01', 220.00, 'Prepaid'),
(3, '2026-10-02', 160.00, 'Cash on Delivery'),
(4, '2026-10-02', 300.00, 'Cash on Delivery'),
(5, '2026-10-03', 240.00, 'Prepaid'),
(6, '2026-10-03', 190.00, 'Cash on Delivery');

INSERT INTO order_items (order_id, menu_id, quantity, price, subtotal)VALUES
(1, 1, 2, 180.00, 360.00),
(2, 2, 1, 140.00, 140.00),
(3, 3, 2, 80.00, 160.00),
(4, 5, 2, 150.00, 300.00),
(5, 4, 1, 160.00, 160.00),
(6, 6, 1, 40.00, 40.00);