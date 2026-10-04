CREATE DATABASE employee_leave_management;

USE employee_leave_management;

-- 1. Employees Table
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15)
);

-- 2. Leave Requests Table
CREATE TABLE leave_requests (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    leave_status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 3. Leave Balance Table
CREATE TABLE leave_balance (
    balance_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    total_leaves INT NOT NULL,
    used_leaves INT DEFAULT 0,
    remaining_leaves INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);