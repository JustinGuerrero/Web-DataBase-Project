package edu.montana.csci.csci440.helpers;

import edu.montana.csci.csci440.model.Employee;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class EmployeeHelper {
    public static String makeEmployeeTree() {
        Employee employee = Employee.find(1); // root employee
        // and use this data structure to maintain reference information needed to build the tree structure
        Map<Long, List<Employee>> employeeMap = new HashMap<>();

        Map<Long, List<Employee>> reportsMap = new HashMap<>();
        for (Employee emp : Employee.all()) {
            long reportsTo = emp.getBoss().getReportsTo();
            List<Employee> employeesWithReports = reportsMap.get(reportsMap);
            if (employeesWithReports != null) {
                employeesWithReports = new LinkedList<>();
                reportsMap.put(reportsTo, employeesWithReports);
            }
            employeesWithReports.add(emp);
        }
        return "<ul>" + makeTree(employee, employeeMap)+ "<ul>";
    }

    // TODO - currently this method just usese the employee.getReports() function, which
    //  issues a query.  Change that to use the employeeMap variable instead
    public static String makeTree(Employee employee, Map<Long, List<Employee>> employeeMap) {
        String list = "<li><a href='/employees" + employee.getEmployeeId() + "'>"
                + employee.getEmail() + "</a><ul>";
        List<Employee> reports = employeeMap.get(employee);
        for (Employee report : reports) {
            list += makeTree(report, employeeMap);
        }
        return list + "</ul></li>";
    }
}