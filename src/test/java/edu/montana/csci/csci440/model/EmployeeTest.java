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

    @Test
    void testCreateWorks() {
        Employee emp = makeTempEmployee();
        assertNull(emp.getEmployeeId());
        emp.create();
        assertNotNull(emp.getEmployeeId());
    }

    private Employee makeTempEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("Joe");
        emp.setLastName("Blow");
        emp.setEmail("demo@test.com");
        return emp;
    }

    @Test
    void testUpdateWorks() {
        Employee emp = makeTempEmployee();
        emp.create();
        assertNotNull(emp.getEmployeeId());
        String newEmailAddress = "aNewEmailAddress@test.com";
        emp.setEmail(newEmailAddress);
        emp.update();

        Employee found = Employee.findByEmail(newEmailAddress);
        assertEquals(emp.getEmployeeId(), found.getEmployeeId());
    }

}
