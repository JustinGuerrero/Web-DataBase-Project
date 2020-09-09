package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Employee extends Model {

    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;

    public Employee() {
        // new employee for insert
    }

    private Employee(ResultSet results) throws SQLException {
        firstName = results.getString("FirstName");
        lastName = results.getString("LastName");
        email = results.getString("Email");
    }

    public static List<Employee> all(int page, int count) {
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet results = stmt.executeQuery("SELECT * FROM employees");
            List<Employee> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Employee(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Employee findByEmail(String newEmailAddress) {
        throw new UnsupportedOperationException("Implement me");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public List<Customer> getCustomers() {
        return Collections.emptyList();
    }

    public List<Employee> getReports() {
        return Collections.emptyList();
    }

    public Employee getBoss() {
        return null;
    }

}
