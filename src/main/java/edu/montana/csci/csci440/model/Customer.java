package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Customer extends Model {

    private Long customerId;
    private Long supportRepId;
    private String firstName;
    private String lastName;
    private String email;

    public Employee getSupportRep() {
         return Employee.find(supportRepId);
    }

    public List<Invoice> getInvoices(){
        return Collections.emptyList();
    }

    public Customer(){}

    private Customer(ResultSet results) throws SQLException {
        firstName = results.getString("FirstName");
        lastName = results.getString("LastName");
        customerId = results.getLong("CustomerId");
        supportRepId = results.getLong("SupportRepId");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getSupportRepId() {
        return supportRepId;
    }

    public static List<Customer> all() {
        return all(0, Integer.MAX_VALUE);
    }

    public static List<Customer> all(int page, int count) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM customers LIMIT ? OFFSET ?"
             )) {
            stmt.setInt(1, count);
            stmt.setInt(2, (page-1)*count);
            ResultSet results = stmt.executeQuery();
            List<Customer> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Customer(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Customer find(long customerId) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE CustomerId=?")) {
            stmt.setLong(1, customerId);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return new Customer(results);
            } else {
                return null;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<Customer> forEmployee(long employeeId) {
        String query = "SELECT * FROM customers WHERE SupportRepId=?";
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, employeeId);
            ResultSet results = stmt.executeQuery();
            List<Customer> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Customer(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
    @Override
    public boolean verify() {
        if (firstName == null || "".equals(firstName)) {
            addError("Name can't be null or blank!");
        }
        if (lastName == null || "".equals(lastName)) {
            addError("Name can't be null or blank!");
        }
        return !hasErrors();
    }

    @Override
    public boolean create() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO customers (FirstName, LastName, Email) VALUES (?,?, ?)")) {
                stmt.setString(1, this.getFirstName());
                stmt.setString(2, getLastName());
                stmt.setString(3, getEmail());
                stmt.executeUpdate();
                customerId = DB.getLastID(conn);
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean update() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE customers  SET FirstName=?, LastName=? WHERE (?,?)")) {
                stmt.setString(1, this.getFirstName());
                stmt.setString(2, this.getLastName());
                stmt.executeUpdate();
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public void delete() {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM customers WHERE CustomerId=?")) {
            stmt.setLong(1, this.getCustomerId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
