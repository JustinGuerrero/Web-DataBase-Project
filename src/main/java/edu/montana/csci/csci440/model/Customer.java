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

    private long customerId;
    private long supportRepId;
    private String firstName;
    private String lastName;
    private String email;

    public Employee getSupportRep() {
         return Employee.find(supportRepId);
    }

    public List<Invoice> getInvoices(){
        return Collections.emptyList();
    }

    private Customer(ResultSet results) throws SQLException {
        firstName = results.getString("FirstName");
        lastName = results.getString("LastName");
        email = results.getString("Email");
        customerId = results.getLong("CustomerId");
        supportRepId = results.getLong("SupportRepId");
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }

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

    public long getCustomerId() {
        return customerId;
    }

    public long getSupportRepId() {
        return supportRepId;
    }

    public static List<Customer> all(int page, int count) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM customers LIMIT ?"
             )) {
            stmt.setInt(1, count);
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

    public static Customer findByEmail(String newEmailAddress) {
        throw new UnsupportedOperationException("Implement me");
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

}
