package com.OnlineFoodOrderingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class OnlineFoodOrderingSystem {

    static String URL = "jdbc:mysql://localhost:3306/online_food_ordering";
    static String USER = "root";
    static String PASSWORD = "root";

    static PreparedStatement ps = null;
    static ResultSet rs = null;

    public static void main(String[] args) {

        Connection con = null;
        Scanner sc = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USER, PASSWORD);

            con.setAutoCommit(false);

            sc = new Scanner(System.in);

            int choice;

            do {

                System.out.println("\n");
                System.out.println("       ONLINE FOOD ORDERING SYSTEM");
                System.out.println("-----------------------------------------");
                System.out.println("1. Customer Registration");
                System.out.println("2. View Menu");
                System.out.println("3. Place Order");
                System.out.println("4. Order History");
                System.out.println("5. Bill Generation");
                System.out.println("6. Exit");
                System.out.println("-----------------------------------------");

                System.out.print("Enter your choice: ");

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                case 1:
                    customerRegistration(con, sc);
                    break;

                case 2:
                    viewMenu(con);
                    break;

                case 3:
                    placeOrder(con, sc);
                    break;

                case 4:
                    orderHistory(con, sc);
                    break;

                case 5:
                    billGeneration(con, sc);
                    break;

                case 6:
                    System.out.println("Exiting Online Food Ordering System...");
                    break;

                default:
                    System.out.println("Invalid choice.");
                }

            } while (choice != 6);

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Database error occurred.");
            e.printStackTrace();

        } catch (Exception e) {

            System.out.println("Invalid input.");
            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }

                if (sc != null) {
                    sc.close();
                }

                if (con != null) {
                    con.close();
                }

                System.out.println(
                        "Resources closed successfully.");

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
    // CUSTOMER REGISTRATION
    
    public static void customerRegistration(
            Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- CUSTOMER REGISTRATION ----------");

            System.out.print("Enter customer name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter phone: ");
            String phone = sc.nextLine();

            String sql ="INSERT INTO customers (customer_name, email, phone) VALUES (?, ?, ?)";

            ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println("Customer registered successfully.");

            } else {

                con.rollback();

                System.out.println("Customer registration failed.");
            }

            ps.close();
            ps = null;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Customer registration failed.");

            e.printStackTrace();
        }
    }
    // VIEW MENU
    
    public static void viewMenu(Connection con) {

        try {

            System.out.println("\n---------------- MENU ----------------");

            String sql = "SELECT * FROM menu";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");
                System.out.println("Menu ID : " + rs.getInt("menu_id"));
                System.out.println("Item    : " + rs.getString("item_name"));
                System.out.println("Price   : ₹" + rs.getDouble("price"));
            }

            if (!found) {

                System.out.println("Menu is empty.");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            System.out.println("Unable to display menu.");

            e.printStackTrace();
        }
    }
    // PLACE ORDER
    
    public static void placeOrder(
            Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- PLACE ORDER ----------");

            String customerSql = "SELECT * FROM customers";

            ps = con.prepareStatement(customerSql);

            rs = ps.executeQuery();

            System.out.println("\nAvailable Customers:");

            while (rs.next()) {
            	System.out.println(rs.getInt("customer_id") + " - " + rs.getString("customer_name"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            // Enter customer ID

            System.out.print("\nEnter customer ID: "); 
            
            int customerId = Integer.parseInt(sc.nextLine()); 
            
            String checkCustomerSql = "SELECT customer_name FROM customers WHERE customer_id = ?";
            
            ps = con.prepareStatement(checkCustomerSql);
            
            ps.setInt(1, customerId);

            rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Customer not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            String customerName = rs.getString("customer_name");

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            // Create order

            String orderSql ="INSERT INTO orders (customer_id, order_date, total_amount, payment_method) VALUES (?, CURDATE(), ?, ?)";

            ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, customerId);

            ps.setDouble(2, 0);

            ps.setString(3, "Pending");

            ps.executeUpdate();

            // Get generated order ID

            rs = ps.getGeneratedKeys();

            int orderId = 0;

            if (rs.next()) {

                orderId = rs.getInt(1);
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            double totalAmount = 0;

            String continueOrder = "";


            do {

                viewMenu(con);

                System.out.print("\nEnter menu ID: ");
                int menuId = Integer.parseInt(sc.nextLine());
                
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(sc.nextLine());
                
                if (quantity <= 0) {
                	
                    System.out.println("Quantity must be greater than zero.");
                
                    continue;
                }

                String priceSql ="SELECT item_name, price FROM menu WHERE menu_id = ?";

                ps = con.prepareStatement(priceSql);

                ps.setInt(1, menuId);

                rs = ps.executeQuery();

                if (!rs.next()) {

                    System.out.println("Menu item not found.");

                    rs.close();
                    rs = null;

                    ps.close();
                    ps = null;

                    continue;
                }

                String itemName =
                        rs.getString("item_name");

                double price =
                        rs.getDouble("price");

                rs.close();
                rs = null;

                ps.close();
                ps = null;


                double subtotal =price * quantity;

                String itemSql ="INSERT INTO order_items (order_id, menu_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";

                ps = con.prepareStatement(itemSql);

                ps.setInt(1, orderId);

                ps.setInt(2, menuId);

                ps.setInt(3, quantity);

                ps.setDouble(4, price);

                ps.setDouble(5, subtotal);

                ps.executeUpdate();

                ps.close();
                ps = null;

                totalAmount =
                        totalAmount + subtotal;

                System.out.println(
                        "\nItem added successfully.");

                System.out.println(
                        "Item     : " + itemName);

                System.out.println(
                        "Quantity : " + quantity);

                System.out.println(
                        "Price    : ₹" + price);

                System.out.println(
                        "Subtotal : ₹" + subtotal);

                System.out.print(
                        "\nDo you want to add another item? (yes/no): ");

                continueOrder =
                        sc.nextLine();

            } while (
                    continueOrder.equalsIgnoreCase("yes"));

            // Payment method

            System.out.println("\n---------- PAYMENT ----------");

            System.out.println("1. Prepaid");

            System.out.println("2. Cash on Delivery");

            System.out.print("Enter payment choice: ");

            int paymentChoice =Integer.parseInt(sc.nextLine());

            String paymentMethod;

            if (paymentChoice == 1) {

                paymentMethod = "Prepaid";

            } else if (paymentChoice == 2) {

                paymentMethod = "Cash on Delivery";

            } else {

                System.out.println("Invalid payment choice.");

                con.rollback();

                return;
            }

            // Update total amount and payment method

            String updateOrderSql = "UPDATE orders SET total_amount = ?, payment_method = ? WHERE order_id = ?";

            ps = con.prepareStatement(updateOrderSql);

            ps.setDouble(1, totalAmount);
            ps.setString(2, paymentMethod);
            ps.setInt(3, orderId);

            ps.executeUpdate();

            ps.close();
            ps = null;

            // Commit transaction

            con.commit();

            System.out.println("\n--------------------------------------");
            System.out.println("       ORDER PLACED SUCCESSFULLY");
            System.out.println("--------------------------------------");
            System.out.println("Order ID      : " + orderId);
            System.out.println("Customer Name : " + customerName);
            System.out.println("Total Amount  : ₹" + totalAmount);
            System.out.println("Payment       : " + paymentMethod);
            System.out.println("--------------------------------------");
            
        } catch (SQLException e) {

            try {

                con.rollback();

            } catch (SQLException ex) {

                ex.printStackTrace();
            }

            System.out.println(
                    "Order failed.");

            e.printStackTrace();

        } catch (Exception e) {

            try {

                con.rollback();

            } catch (SQLException ex) {

                ex.printStackTrace();
            }

            System.out.println(
                    "Invalid input.");

            e.printStackTrace();
        }
    }
    
    // ORDER HISTORY
    
    public static void orderHistory(
            Connection con, Scanner sc) {

        try {

        	System.out.println("\n---------- ORDER HISTORY ----------");
        	System.out.print("Enter customer ID: ");
        	int customerId = Integer.parseInt(sc.nextLine());
        	String sql = "SELECT o.order_id, o.order_date, o.total_amount, o.payment_method, c.customer_name, m.item_name, oi.quantity FROM orders o INNER JOIN customers c ON o.customer_id = c.customer_id INNER JOIN order_items oi ON o.order_id = oi.order_id INNER JOIN menu m ON oi.menu_id = m.menu_id WHERE o.customer_id = ? ORDER BY o.order_id";
        	ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");
                System.out.println("Order ID      : " + rs.getInt("order_id"));
                System.out.println("Customer Name : " + rs.getString("customer_name"));
                System.out.println("Order Date    : " + rs.getDate("order_date"));
                System.out.println("Item Name     : " + rs.getString("item_name"));
                System.out.println("Quantity      : " + rs.getInt("quantity"));
                System.out.println("Total Amount  : ₹" + rs.getDouble("total_amount"));
                System.out.println("Payment       : " + rs.getString("payment_method"));
            }

            if (!found) {

                System.out.println(
                        "No orders found for this customer.");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            System.out.println(
                    "Unable to display order history.");

            e.printStackTrace();
        }
    }
    
    // BILL GENERATION
    
    public static void billGeneration(
            Connection con, Scanner sc) {

        try {

        	System.out.println("\n---------- BILL GENERATION ----------");
        	System.out.print("Enter order ID: ");
        	int orderId = Integer.parseInt(sc.nextLine());

        	// Get order details

            String orderSql ="SELECT o.order_id, o.order_date, c.customer_name, c.phone, o.total_amount, o.payment_method FROM orders o INNER JOIN customers c ON o.customer_id = c.customer_id WHERE o.order_id = ?";

            ps = con.prepareStatement(orderSql);

            ps.setInt(1, orderId);

            rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Order not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            String customerName =rs.getString("customer_name");

            String phone = rs.getString("phone");

            java.sql.Date orderDate =rs.getDate("order_date");

            double totalAmount = rs.getDouble("total_amount");

            String paymentMethod =rs.getString("payment_method");

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            // Display bill

            System.out.println("\n--------------------------------------");
            System.out.println("              FOOD BILL");
            System.out.println("---------------------------------------");
            System.out.println("Order ID      : " + orderId);
            System.out.println("Customer Name : " + customerName);
            System.out.println("Phone         : " + phone);
            System.out.println("Order Date    : " + orderDate);
            System.out.println("Payment       : " + paymentMethod);
            System.out.println("--------------------------------------");

            // Get order items
            String itemSql = "SELECT oi.order_item_id, m.item_name, oi.quantity, oi.price, oi.subtotal FROM order_items oi INNER JOIN menu m ON oi.menu_id = m.menu_id WHERE oi.order_id = ?";
            ps = con.prepareStatement(itemSql);

            ps.setInt(1, orderId);

            rs = ps.executeQuery();

            while (rs.next()) {

            	System.out.println(rs.getInt("order_item_id") + ". " + rs.getString("item_name") 
            	+ " | Qty: "+rs.getInt("quantity")
            	+ " | Price: ₹" + rs.getDouble("price") 
            	+ " | Subtotal: ₹" + rs.getDouble("subtotal"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            System.out.println("--------------------------------------");
            System.out.println("TOTAL BILL    : ₹" + totalAmount);
            System.out.println("PAYMENT METHOD: " + paymentMethod);
            System.out.println("--------------------------------------");

        } catch (SQLException e) {

            System.out.println(
                    "Unable to generate bill.");

            e.printStackTrace();
        }
    }
}