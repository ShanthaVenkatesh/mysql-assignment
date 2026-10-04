package com.Employee_leave_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmployeeLeaveManagementSystem {

    static String URL = "jdbc:mysql://localhost:3306/employee_leave_management";
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
            	System.out.println();
            	System.out.println("     EMPLOYEE LEAVE MANAGEMENT SYSTEM");
                System.out.println("---------------------------------------------");
                System.out.println("1. Register Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Search Employee");
                System.out.println("4. Update Employee");
                System.out.println("5. Delete Employee");
                System.out.println("6. Apply Leave");
                System.out.println("7. Approve Leave");
                System.out.println("8. Reject Leave");
                System.out.println("9. Show Leave Balance");
                System.out.println("10. Exit");
                System.out.println("---------------------------------------------");
                System.out.print("Enter your choice: ");

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        registerEmployee(con, sc);
                        break;

                    case 2:
                        viewAllEmployees(con);
                        break;

                    case 3:
                        searchEmployee(con, sc);
                        break;

                    case 4:
                        updateEmployee(con, sc);
                        break;

                    case 5:
                        deleteEmployee(con, sc);
                        break;

                    case 6:
                        applyLeave(con, sc);
                        break;

                    case 7:
                        approveLeave(con, sc);
                        break;

                    case 8:
                        rejectLeave(con, sc);
                        break;

                    case 9:
                        showLeaveBalance(con, sc);
                        break;

                    case 10:
                        System.out.println("Exiting Employee Leave Management System...");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 10);

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

                System.out.println("Resources closed successfully.");

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
    //1. REGISTER EMPLOYEE
    
    public static void registerEmployee(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- REGISTER EMPLOYEE ----------");

            System.out.print("Enter employee name: ");
            String name = sc.nextLine();

            System.out.print("Enter employee email: ");
            String email = sc.nextLine();

            System.out.print("Enter employee phone: ");
            String phone = sc.nextLine();

            String sql ="INSERT INTO employees (employee_name, email, phone) VALUES (?, ?, ?)";

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                rs = ps.getGeneratedKeys();

                int employeeId = 0;

                if (rs.next()) {
                    employeeId = rs.getInt(1);
                }

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                // Create leave balance automatically
                String balanceSql = "INSERT INTO leave_balance (employee_id, total_leaves, used_leaves, remaining_leaves) VALUES (?, ?, ?, ?)";

                ps = con.prepareStatement(balanceSql);

                ps.setInt(1, employeeId);
                ps.setInt(2, 20);
                ps.setInt(3, 0);
                ps.setInt(4, 20);

                ps.executeUpdate();

                ps.close();
                ps = null;

                con.commit();

                System.out.println("Employee registered successfully.");
                System.out.println("Employee ID: " + employeeId);
                System.out.println("Total Leave: 20");
                System.out.println("Used Leave: 0");
                System.out.println("Remaining Leave: 20");

            } else {

                con.rollback();

                System.out.println("Employee registration failed.");
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Employee registration failed.");
            e.printStackTrace();
        }
    }
    // 2. VIEW ALL EMPLOYEES
    
    public static void viewAllEmployees(Connection con) {

        try {

            System.out.println("\n---------- ALL EMPLOYEES ----------");

            String sql = "SELECT * FROM employees";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("------------------------------------------");
                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Email         : " + rs.getString("email"));
                System.out.println("Phone         : " + rs.getString("phone"));
            }

            if (!found) {
                System.out.println("No employees found.");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            System.out.println("Unable to display employees.");
            e.printStackTrace();
        }
    }
    
    // 3. SEARCH EMPLOYEE
    
    public static void searchEmployee(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- SEARCH EMPLOYEE ----------");

            System.out.print("Enter employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT * FROM employees WHERE employee_id = ?";

            ps = con.prepareStatement(sql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("------------------------------------------");
                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Email         : " + rs.getString("email"));
                System.out.println("Phone         : " + rs.getString("phone"));

            } else {

                System.out.println("Employee not found.");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            System.out.println("Unable to search employee.");
            e.printStackTrace();
        }
    }
    
    // 4. UPDATE EMPLOYEE
    
    public static void updateEmployee(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- UPDATE EMPLOYEE ----------");

            System.out.print("Enter employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine());

            String checkSql =
                    "SELECT employee_id FROM employees WHERE employee_id = ?";

            ps = con.prepareStatement(checkSql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Employee not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            System.out.print("Enter employee name: ");
            String name = sc.nextLine();

            System.out.print("Enter employee email: ");
            String email = sc.nextLine();

            System.out.print("Enter employee phone: ");
            String phone = sc.nextLine();

            String sql = "UPDATE employees SET employee_name = ?, email = ?, phone = ? WHERE employee_id = ?";

            ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, employeeId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println("Employee updated successfully.");

            } else {

                con.rollback();

                System.out.println("Employee update failed.");
            }

            ps.close();
            ps = null;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Employee update failed.");
            e.printStackTrace();
        }
    }
    // 5. DELETE EMPLOYEE
    
    public static void deleteEmployee(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- DELETE EMPLOYEE ----------");

            System.out.print("Enter employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine());

            String deleteRequests =
                    "DELETE FROM leave_requests WHERE employee_id = ?";

            ps = con.prepareStatement(deleteRequests);

            ps.setInt(1, employeeId);

            ps.executeUpdate();

            ps.close();
            ps = null;

            String deleteBalance =
                    "DELETE FROM leave_balance WHERE employee_id = ?";

            ps = con.prepareStatement(deleteBalance);

            ps.setInt(1, employeeId);

            ps.executeUpdate();

            ps.close();
            ps = null;

            String deleteEmployee =
                    "DELETE FROM employees WHERE employee_id = ?";

            ps = con.prepareStatement(deleteEmployee);

            ps.setInt(1, employeeId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println("Employee deleted successfully.");

            } else {

                con.rollback();

                System.out.println("Employee not found.");
            }

            ps.close();
            ps = null;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Employee deletion failed.");
            e.printStackTrace();
        }
    }

    // 6. APPLY LEAVE
   
    public static void applyLeave(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- APPLY LEAVE ----------");

            String employeeSql = "SELECT employee_id, employee_name FROM employees";
            ps = con.prepareStatement(employeeSql);

            rs = ps.executeQuery();

            System.out.println("\nAvailable Employees:");

            while (rs.next()) {

            	System.out.println(rs.getInt("employee_id") + " - " + rs.getString("employee_name"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            System.out.print("\nEnter employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine());

            // Get employee name
            String nameSql =
                    "SELECT employee_name "
                    + "FROM employees "
                    + "WHERE employee_id = ?";

            ps = con.prepareStatement(nameSql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            String employeeName = null;

            if (rs.next()) {

                employeeName = rs.getString("employee_name");

            } else {

                System.out.println("Employee not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            System.out.println("\nLeave Types:");
            System.out.println("1. Casual Leave");
            System.out.println("2. Sick Leave");
            System.out.println("3. Earned Leave");
            System.out.println("4. Emergency Leave");
            System.out.println("5. Vacation Leave");

            System.out.print("Enter leave type: ");
            String leaveType = sc.nextLine();

            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDate = sc.nextLine();

            System.out.print("Enter end date (YYYY-MM-DD): ");
            String endDate = sc.nextLine();

            System.out.print("Enter reason: ");
            String reason = sc.nextLine();

            String sql =
                    "INSERT INTO leave_requests "
                    + "(employee_id, employee_name, leave_type, start_date, "
                    + "end_date, reason, leave_status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);

            ps.setInt(1, employeeId);
            ps.setString(2, employeeName);
            ps.setString(3, leaveType);
            ps.setDate(4, java.sql.Date.valueOf(startDate));
            ps.setDate(5, java.sql.Date.valueOf(endDate));
            ps.setString(6, reason);
            ps.setString(7, "Pending");

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println("\nLeave applied successfully.");
                System.out.println("Employee Name : " + employeeName);
                System.out.println("Leave Status  : Pending");

            } else {

                con.rollback();

                System.out.println("Leave application failed.");
            }

            ps.close();
            ps = null;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Leave application failed.");
            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid date format.");
            System.out.println("Please use YYYY-MM-DD.");
        }
    }

    // =========================================================
    // 7. APPROVE LEAVE
    // =========================================================

    public static void approveLeave(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- APPROVE LEAVE ----------");

            // Display pending leaves
            String pendingSql =
                    "SELECT leave_id, employee_id, employee_name, "
                    + "leave_type, start_date, end_date, reason "
                    + "FROM leave_requests "
                    + "WHERE leave_status = 'Pending'";

            ps = con.prepareStatement(pendingSql);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("------------------------------------------");
                System.out.println("Leave ID      : " + rs.getInt("leave_id"));
                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Leave Type    : " + rs.getString("leave_type"));
                System.out.println("Start Date    : " + rs.getDate("start_date"));
                System.out.println("End Date      : " + rs.getDate("end_date"));
                System.out.println("Reason        : " + rs.getString("reason"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (!found) {

                System.out.println("No pending leave requests.");
                return;
            }

            System.out.print("\nEnter leave ID to approve: ");
            int leaveId = Integer.parseInt(sc.nextLine());

            // Get leave details
            String detailsSql =
                    "SELECT employee_id, start_date, end_date, leave_status "
                    + "FROM leave_requests "
                    + "WHERE leave_id = ?";

            ps = con.prepareStatement(detailsSql);

            ps.setInt(1, leaveId);

            rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Leave request not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            int employeeId = rs.getInt("employee_id");
            java.sql.Date startDate = rs.getDate("start_date");
            java.sql.Date endDate = rs.getDate("end_date");
            String status = rs.getString("leave_status");

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (!status.equalsIgnoreCase("Pending")) {

                System.out.println("This leave is already " + status + ".");

                return;
            }

            // Calculate number of leave days
            String daysSql =
                    "SELECT DATEDIFF(?, ?) + 1 AS total_days";

            ps = con.prepareStatement(daysSql);

            ps.setDate(1, endDate);
            ps.setDate(2, startDate);

            rs = ps.executeQuery();

            int days = 0;

            if (rs.next()) {
                days = rs.getInt("total_days");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            // Check balance
            String balanceSql =
                    "SELECT remaining_leaves "
                    + "FROM leave_balance "
                    + "WHERE employee_id = ?";

            ps = con.prepareStatement(balanceSql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            int remainingLeaves = 0;

            if (rs.next()) {

                remainingLeaves = rs.getInt("remaining_leaves");

            } else {

                System.out.println("Leave balance not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (days > remainingLeaves) {

                System.out.println("Insufficient leave balance.");
                System.out.println("Requested Days : " + days);
                System.out.println("Remaining Days : " + remainingLeaves);

                return;
            }

            // Update leave status
            String approveSql =
                    "UPDATE leave_requests "
                    + "SET leave_status = 'Approved' "
                    + "WHERE leave_id = ?";

            ps = con.prepareStatement(approveSql);

            ps.setInt(1, leaveId);

            ps.executeUpdate();

            ps.close();
            ps = null;

            // Update leave balance
            String updateBalanceSql =
                    "UPDATE leave_balance "
                    + "SET used_leaves = used_leaves + ?, "
                    + "remaining_leaves = remaining_leaves - ? "
                    + "WHERE employee_id = ?";

            ps = con.prepareStatement(updateBalanceSql);

            ps.setInt(1, days);
            ps.setInt(2, days);
            ps.setInt(3, employeeId);

            ps.executeUpdate();

            ps.close();
            ps = null;

            con.commit();

            System.out.println("\nLeave approved successfully.");
            System.out.println("Leave ID       : " + leaveId);
            System.out.println("Approved Days  : " + days);

            // Show updated balance
            String showBalanceSql =
                    "SELECT total_leaves, used_leaves, remaining_leaves "
                    + "FROM leave_balance "
                    + "WHERE employee_id = ?";

            ps = con.prepareStatement(showBalanceSql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\n---------- UPDATED LEAVE BALANCE ----------");
                System.out.println("Total Leaves      : " + rs.getInt("total_leaves"));
                System.out.println("Used Leaves       : " + rs.getInt("used_leaves"));
                System.out.println("Remaining Leaves  : " + rs.getInt("remaining_leaves"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Leave approval failed.");
            e.printStackTrace();
        }
    }

    // =========================================================
    // 8. REJECT LEAVE
    // =========================================================

    public static void rejectLeave(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- REJECT LEAVE ----------");

            // Display pending leaves
            String pendingSql =
                    "SELECT leave_id, employee_id, employee_name, "
                    + "leave_type, start_date, end_date, reason "
                    + "FROM leave_requests "
                    + "WHERE leave_status = 'Pending'";

            ps = con.prepareStatement(pendingSql);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("------------------------------------------");
                System.out.println("Leave ID      : " + rs.getInt("leave_id"));
                System.out.println("Employee ID   : " + rs.getInt("employee_id"));
                System.out.println("Employee Name : " + rs.getString("employee_name"));
                System.out.println("Leave Type    : " + rs.getString("leave_type"));
                System.out.println("Start Date    : " + rs.getDate("start_date"));
                System.out.println("End Date      : " + rs.getDate("end_date"));
                System.out.println("Reason        : " + rs.getString("reason"));
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (!found) {

                System.out.println("No pending leave requests.");
                return;
            }

            System.out.print("\nEnter leave ID to reject: ");
            int leaveId = Integer.parseInt(sc.nextLine());

            // Get employee ID and status
            String detailsSql =
                    "SELECT employee_id, leave_status "
                    + "FROM leave_requests "
                    + "WHERE leave_id = ?";

            ps = con.prepareStatement(detailsSql);

            ps.setInt(1, leaveId);

            rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println("Leave request not found.");

                rs.close();
                rs = null;

                ps.close();
                ps = null;

                return;
            }

            int employeeId = rs.getInt("employee_id");
            String status = rs.getString("leave_status");

            rs.close();
            rs = null;

            ps.close();
            ps = null;

            if (!status.equalsIgnoreCase("Pending")) {

                System.out.println("This leave is already " + status + ".");

                return;
            }

            // Reject leave
            String rejectSql =
                    "UPDATE leave_requests "
                    + "SET leave_status = 'Rejected' "
                    + "WHERE leave_id = ?";

            ps = con.prepareStatement(rejectSql);

            ps.setInt(1, leaveId);

            int rows = ps.executeUpdate();

            ps.close();
            ps = null;

            if (rows > 0) {

                con.commit();

                System.out.println("\nLeave rejected successfully.");

                // Show leave balance after rejection
                String balanceSql =
                        "SELECT total_leaves, used_leaves, remaining_leaves "
                        + "FROM leave_balance "
                        + "WHERE employee_id = ?";

                ps = con.prepareStatement(balanceSql);

                ps.setInt(1, employeeId);

                rs = ps.executeQuery();

                if (rs.next()) {

                    System.out.println("\n---------- LEAVE BALANCE AFTER REJECTION ----------");
                    System.out.println("Total Leaves      : " + rs.getInt("total_leaves"));
                    System.out.println("Used Leaves       : " + rs.getInt("used_leaves"));
                    System.out.println("Remaining Leaves  : " + rs.getInt("remaining_leaves"));
                    System.out.println("\nRejected leave does not reduce the leave balance.");
                }

                rs.close();
                rs = null;

                ps.close();
                ps = null;

            } else {

                con.rollback();

                System.out.println("Leave rejection failed.");
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.out.println("Leave rejection failed.");
            e.printStackTrace();
        }
    }
    // 9. SHOW LEAVE BALANCE
    
    public static void showLeaveBalance(Connection con, Scanner sc) {

        try {

            System.out.println("\n---------- SHOW LEAVE BALANCE ----------");

            System.out.print("Enter employee ID: ");
            int employeeId = Integer.parseInt(sc.nextLine());

            String sql = "SELECT e.employee_id, e.employee_name, lb.total_leaves, lb.used_leaves, lb.remaining_leaves"
            		+" FROM employees e INNER JOIN leave_balance lb ON e.employee_id = lb.employee_id"
            		+" WHERE e.employee_id = ?";

            ps = con.prepareStatement(sql);

            ps.setInt(1, employeeId);

            rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("------------------------------------------");
                System.out.println("Employee ID       : " + rs.getInt("employee_id"));
                System.out.println("Employee Name     : " + rs.getString("employee_name"));
                System.out.println("Total Leaves      : " + rs.getInt("total_leaves"));
                System.out.println("Used Leaves       : " + rs.getInt("used_leaves"));
                System.out.println("Remaining Leaves  : " + rs.getInt("remaining_leaves"));

            } else {

                System.out.println("Employee or leave balance not found.");
            }

            rs.close();
            rs = null;

            ps.close();
            ps = null;

        } catch (SQLException e) {

            System.out.println("Unable to display leave balance.");
            e.printStackTrace();
        }
    }
}