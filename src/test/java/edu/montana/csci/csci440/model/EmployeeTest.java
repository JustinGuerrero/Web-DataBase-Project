package edu.montana.csci.csci440.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest extends ModelTest {

    @Test
    void testAllLoadsAllEmployees() {
        List<Employee> all = Employee.all(0, Integer.MAX_VALUE);
        assertEquals(8, all.size());
    }

    @Test
    void testPagingWorks() {
        List<Employee> all = Employee.all(0, 2);
        assertEquals(2, all.size());
    }

}
