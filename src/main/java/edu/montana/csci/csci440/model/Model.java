package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// base class for entities
public class Model {

    List<String> _errors = new LinkedList<>();

    public boolean create()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean update()
    {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE employees SET FirstName=?, LastName=?, Email=? WHERE EmployeeId=?")) {
                stmt.setString(1, this.getFirstName());
                stmt.setString(2, this.getLastName());
                stmt.setString(3, this.getEmail());
                stmt.setLong(4, this.getEmployeeId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            throw new UnsupportedOperationException("Error: that didn't work");
            return false;
    }
    }

    public void delete()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean verify()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void addError(String err)
    {
        _errors.add(err);
    }

    public List<String> getErrors()
    {
        return _errors;
    }

    public boolean hasErrors()
    {
        return _errors.size() > 0;
    }

}
