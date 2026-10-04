# JDBC Management Systems

This repository contains three Java JDBC-based management system projects developed using **Java, JDBC, and MySQL**.

The projects demonstrate important JDBC concepts such as **CRUD operations, PreparedStatement, ResultSet, transactions, joins, master-detail relationships, validation, and database connectivity**.

## Projects Included

### 1. Student Management System

The Student Management System is used to manage student academic information.

#### Features
- Student Registration
- View Student Details
- Search Student
- Update Student Information
- Delete Student
- Course Management
- Student Enrollment
- Attendance Management
- Marks Management
- Validation for attendance and marks
- Generate student results

#### JDBC Concepts
- Database Connectivity
- CRUD Operations
- PreparedStatement
- ResultSet
- SQL Joins
- Transactions
- Data Validation

---

### 2. Employee Leave Management System

The Employee Leave Management System is used to manage employee details and leave requests.

#### Features
- Employee Registration
- View Employees
- Search Employee
- Update Employee Details
- Delete Employee
- Apply for Leave
- Approve Leave
- Reject Leave
- Leave Balance Tracking
- Display Remaining Leaves

#### JDBC Concepts
- CRUD Operations
- PreparedStatement
- ResultSet
- Transactions
- Foreign Key Relationships
- Data Validation
- Commit and Rollback

---

### 3. Online Food Ordering System

The Online Food Ordering System allows customers to register, view the food menu, place orders, view order history, and generate bills.

#### Features
- Customer Registration
- View Food Menu
- Place Order
- Add Multiple Food Items
- Order History
- Bill Generation
- Payment Method Selection
  - Prepaid
  - Cash on Delivery
- Automatic Order ID Generation
- Total Amount Calculation

#### JDBC Concepts
- Master-Detail Relationship
- CRUD Operations
- PreparedStatement
- ResultSet
- Transactions
- Commit and Rollback
- Generated Keys
- SQL Joins

## Technologies Used

- **Java**
- **JDBC**
- **MySQL**
- **MySQL Workbench**
- **Eclipse IDE**
- **MySQL Connector/J**
- **Maven**

## Database

The projects use MySQL databases.

### Student Management System
Database contains tables for:
- Students
- Courses
- Enrollments
- Attendance
- Marks

### Employee Leave Management System
Database contains:
- Employees
- Leave Requests
- Leave Balance

### Online Food Ordering System
Database contains:
- Customers
- Menu
- Orders
- Order Items

## JDBC Architecture

The applications follow a simple JDBC-based flow:

```text
Java Application
       |
       v
     JDBC
       |
       v
 MySQL Database
       |
       v
Tables and Records
```

## Important JDBC Concepts Demonstrated

### Connection
Establishes a connection between the Java application and MySQL database.

### PreparedStatement
Used to execute parameterized SQL queries safely.

### ResultSet
Used to retrieve records returned by SELECT queries.

### CRUD Operations

The projects demonstrate:

- **Create** – Insert records
- **Read** – Retrieve records
- **Update** – Modify records
- **Delete** – Remove records

### Transactions

Transactions are used to ensure that related database operations are completed successfully.

```text
Start Transaction
       |
       v
Execute Operations
       |
   Success?
   /     \
 Yes      No
 |         |
Commit   Rollback
```

## Project Objective

The main objective of these projects is to understand how Java applications communicate with relational databases using **JDBC** and to implement real-world management systems using SQL and Java.

These projects provide practical experience with:

- Java programming
- SQL queries
- JDBC connectivity
- Database design
- CRUD operations
- PreparedStatement
- ResultSet
- Transactions
- Joins
- Foreign keys
- Master-detail relationships
- Input validation

## Author

Developed as part of Java and JDBC training and practice.
