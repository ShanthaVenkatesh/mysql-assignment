package com.student_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagementSystem {

    static String URL = "jdbc:mysql://localhost:3306/student_management";
    static String USER = "root";
    static String PASSWORD = "root";

    public static void main(String[] args) {

        Connection con = null;
        Scanner sc = null;

        PreparedStatement ps = null;

        try {

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            // Scanner
            sc = new Scanner(System.in);

            int choice;

            do {

                System.out.println("       STUDENT MANAGEMENT SYSTEM");
                System.out.println("-------------------------------------------");

                System.out.println("1. Student Registration");
                System.out.println("2. View All Students");
                System.out.println("3. Search Student");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Add Course");
                System.out.println("7. Course Enrollment");
                System.out.println("8. View Enrollments");
                System.out.println("9. Attendance Management");
                System.out.println("10. Marks Entry");
                System.out.println("11. Result Generation");
                System.out.println("12. Exit");

                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {

                case 1:
                    registerStudent(con, sc);
                    break;

                case 2:
                    viewAllStudents(con);
                    break;

                case 3:
                    searchStudent(con, sc);
                    break;

                case 4:
                    updateStudent(con, sc);
                    break;

                case 5:
                    deleteStudent(con, sc);
                    break;

                case 6:
                    addCourse(con, sc);
                    break;

                case 7:
                    enrollStudent(con, sc);
                    break;

                case 8:
                    viewEnrollments(con);
                    break;

                case 9:
                    addAttendance(con, sc);
                    break;

                case 10:
                    addMarks(con, sc);
                    break;

                case 11:
                    generateResult(con, sc);
                    break;

                case 12:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
                }

            } while (choice != 12);

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Database error.");
            e.printStackTrace();

        } finally {

            try {

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

    // 1. STUDENT REGISTRATION
    
    public static void registerStudent(Connection con, Scanner sc)throws SQLException {

        String sql = "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student name: ");
        String name = sc.next();

        System.out.print("Enter email: ");
        String email = sc.next();

        System.out.print("Enter phone: ");
        String phone = sc.next();

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Student registered successfully.");
        }

        ps.close();
    }

    // 2. VIEW ALL STUDENTS
    
    public static void viewAllStudents(Connection con)
            throws SQLException {

        String sql = "SELECT * FROM students";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n---------- STUDENT DETAILS ----------");

        while (rs.next()) {

            System.out.println(
                    "Student ID : "
                    + rs.getInt("student_id")
            );

            System.out.println(
                    "Name       : "
                    + rs.getString("name")
            );

            System.out.println(
                    "Email      : "
                    + rs.getString("email")
            );

            System.out.println(
                    "Phone      : "
                    + rs.getString("phone")
            );

            System.out.println("-------------------------------------");
        }

        rs.close();
        ps.close();
    }
    
    // 3. SEARCH STUDENT
    
      public static void searchStudent(Connection con, Scanner sc)
            throws SQLException {

        String sql ="SELECT * FROM students WHERE student_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        ps.setInt(1, studentId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            System.out.println("\nStudent ID : "
                    + rs.getInt("student_id"));

            System.out.println("Name       : "
                    + rs.getString("name"));

            System.out.println("Email      : "
                    + rs.getString("email"));

            System.out.println("Phone      : "
                    + rs.getString("phone"));

        } else {

            System.out.println("Student not found.");
        }

        rs.close();
        ps.close();
    }

   // 4. UPDATE STUDENT
    
    public static void updateStudent(Connection con, Scanner sc)
            throws SQLException {

        String sql ="UPDATE students SET name = ?, email = ?, phone = ? WHERE student_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        System.out.print("Enter new name: ");
        String name = sc.next();

        System.out.print("Enter new email: ");
        String email = sc.next();

        System.out.print("Enter new phone: ");
        String phone = sc.next();

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setInt(4, studentId);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            System.out.println(
                    "Student updated successfully."
            );

        } else {

            System.out.println("Student not found.");
        }

        ps.close();
    }

    // 5. DELETE STUDENT
    
    public static void deleteStudent(Connection con, Scanner sc)
            throws SQLException {

        String sql = "DELETE FROM students WHERE student_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        ps.setInt(1, studentId);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            System.out.println(
                    "Student deleted successfully."
            );

        } else {

            System.out.println("Student not found.");
        }

        ps.close();
    }

    // 6. ADD COURSE
    
    public static void addCourse(Connection con, Scanner sc)
            throws SQLException {

        String sql ="INSERT INTO courses (course_name, course_duration) VALUES (?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter course name: ");
        String courseName = sc.next();

        System.out.print("Enter course duration: ");
        String courseDuration = sc.next();

        ps.setString(1, courseName);
        ps.setString(2, courseDuration);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            System.out.println(
                    "Course added successfully."
            );
        }

        ps.close();
    }

    // 7. COURSE ENROLLMENT
    // TRANSACTION
    public static void enrollStudent(Connection con, Scanner sc)
            throws SQLException {

        // Display available courses first
        String courseSql =
                "SELECT * FROM courses";

        PreparedStatement psCourse =
                con.prepareStatement(courseSql);

        ResultSet rs = psCourse.executeQuery();

        System.out.println("          AVAILABLE COURSES");
        while (rs.next()) {
        	System.out.println("Course ID       : " + rs.getInt("course_id"));
        	System.out.println("Course Name     : " + rs.getString("course_name"));
        	System.out.println("Course Duration : " + rs.getString("course_duration"));
            System.out.println("--------------------------------------");
        }

        rs.close();
        psCourse.close();


        // Enrollment query
        String sql ="INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        try {

            con.setAutoCommit(false);

            System.out.print("Enter student ID: ");
            int studentId = sc.nextInt();

            System.out.print("Enter course ID: ");
            int courseId = sc.nextInt();

            System.out.print("Enter enrollment date (YYYY-MM-DD): ");

            String date = sc.next();

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setString(3, date);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();

                System.out.println("Student enrolled successfully.");
                System.out.println("Transaction committed.");
            }

        } catch (SQLException e) {

            con.rollback();

            System.out.println("Enrollment failed.");
            System.out.println("Transaction rolled back.");

            e.printStackTrace();

        } finally {

            con.setAutoCommit(true);
            ps.close();
        }
    }
    // 8. VIEW ENROLLMENTS
    // JOIN
    
    public static void viewEnrollments(Connection con)
            throws SQLException {

        String sql =
                "SELECT e.enrollment_id,s.student_id,s.name,c.course_id,c.course_name,c.course_duration, e.enrollment_date "
                + "FROM enrollments e JOIN students s "
                + "ON e.student_id = s.student_id "
                + "JOIN courses c "
                + "ON e.course_id = c.course_id";

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        System.out.println("\n---------- ENROLLMENT DETAILS ----------");

        while (rs.next()) {

            System.out.println("Enrollment ID : "+ rs.getInt("enrollment_id"));
            System.out.println("Student ID    : "+ rs.getInt("student_id"));
            System.out.println("Student Name  : "+ rs.getString("name"));
            System.out.println("Course ID     : "+ rs.getInt("course_id"));
            System.out.println("Course Name   : "+ rs.getString("course_name"));
            System.out.println("Duration      : "+ rs.getString("course_duration"));
            System.out.println("Enrollment Date : "+ rs.getDate("enrollment_date"));
            System.out.println("-----------------------------------------");
        }

        rs.close();
        ps.close();
    }
    
    // 9. ATTENDANCE MANAGEMENT
    
    public static void addAttendance(Connection con, Scanner sc)
            throws SQLException {

        String sql ="INSERT INTO attendance (student_id, course_id, total_classes, attended_classes) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        System.out.print("Enter course ID: ");
        int courseId = sc.nextInt();

        System.out.print("Enter total classes: ");
        int totalClasses = sc.nextInt();

        System.out.print("Enter attended classes: ");
        int attendedClasses = sc.nextInt();

        if (totalClasses <= attendedClasses) {

        	System.out.println("Invalid attendance!");
        	System.out.println("Total classes must be greater " + "than attended classes.");

            ps.close();
            return;
        }

        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setInt(3, totalClasses);
        ps.setInt(4, attendedClasses);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Attendance added successfully.");

            double percentage = ((double) attendedClasses / totalClasses) * 100;

            System.out.println("Attendance Percentage : " + percentage + "%");
        }
        
        ps.close();
    }

    // 10. MARKS ENTRY
    
    public static void addMarks(Connection con, Scanner sc)
            throws SQLException {

        String sql ="INSERT INTO marks (student_id, course_id, total_marks, obtained_marks) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        System.out.print("Enter course ID: ");
        int courseId = sc.nextInt();

        System.out.print("Enter total marks: ");
        int totalMarks = sc.nextInt();

        System.out.print("Enter obtained marks: ");
        int obtainedMarks = sc.nextInt();

        if (obtainedMarks > totalMarks) {

        	System.out.println("Invalid marks!");
        	System.out.println("Obtained marks cannot be " + "greater than total marks.");
            ps.close();
            return;
        }

        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setInt(3, totalMarks);
        ps.setInt(4, obtainedMarks);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            System.out.println(
                    "Marks entered successfully."
            );
        }

        ps.close();
    }
    // 11. RESULT GENERATION
    // JOIN
    
    public static void generateResult(Connection con, Scanner sc)
            throws SQLException {

        String sql =
                "SELECT s.student_id, "
                + "s.name, "
                + "c.course_name, "
                + "m.total_marks, "
                + "m.obtained_marks, "
                + "a.total_classes, "
                + "a.attended_classes "
                + "FROM students s "
                + "JOIN enrollments e "
                + "ON s.student_id = e.student_id "
                + "JOIN courses c "
                + "ON e.course_id = c.course_id "
                + "LEFT JOIN marks m "
                + "ON s.student_id = m.student_id "
                + "AND c.course_id = m.course_id "
                + "LEFT JOIN attendance a "
                + "ON s.student_id = a.student_id "
                + "AND c.course_id = a.course_id "
                + "WHERE s.student_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);

        System.out.print("Enter student ID: ");
        int studentId = sc.nextInt();

        ps.setInt(1, studentId);

        ResultSet rs = ps.executeQuery();

        boolean found = false;
        System.out.println("           STUDENT RESULT");
        System.out.println("-------------------------------------");

        while (rs.next()) {

            found = true;

            int totalMarks =
                    rs.getInt("total_marks");

            int obtainedMarks =
                    rs.getInt("obtained_marks");

            int totalClasses =
                    rs.getInt("total_classes");

            int attendedClasses =
                    rs.getInt("attended_classes");

            double marksPercentage = 0;

            if (totalMarks > 0) {

                marksPercentage =
                        ((double) obtainedMarks
                        / totalMarks) * 100;
            }

            double attendancePercentage = 0;

            if (totalClasses > 0) {

                attendancePercentage =
                        ((double) attendedClasses
                        / totalClasses) * 100;
            }

            String result;

            if (marksPercentage >= 40) {
                result = "PASS";
            } else {
                result = "FAIL";
            }

            System.out.println("Student ID          : " + rs.getInt("student_id"));
            System.out.println("Student Name        : " + rs.getString("name"));
            System.out.println("Course              : " + rs.getString("course_name"));
            System.out.println("Total Marks         : " + totalMarks);
            System.out.println("Obtained Marks      : " + obtainedMarks);
            System.out.println("Marks Percentage    : " + marksPercentage + "%");
            System.out.println("Total Classes       : " + totalClasses);
            System.out.println("Attended Classes    : " + attendedClasses);
            System.out.println("Attendance          : " + attendancePercentage + "%");
            System.out.println("Result              : " + result);
            System.out.println("--------------------------------------");
        }

        if (!found) {

            System.out.println(
                    "No result found for this student."
            );
        }

        rs.close();
        ps.close();
    }
}
