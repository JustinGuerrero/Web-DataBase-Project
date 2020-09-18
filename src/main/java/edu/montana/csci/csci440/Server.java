package edu.montana.csci.csci440;

import edu.montana.csci.csci440.model.Employee;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.*;

class Server {

    public static void main(String[] args) {

        // magic...
        before((request, response) -> Web.setReq(request));

        /* ========================================================================= */
        /* Root Path                                                                 */
        /* ========================================================================= */

        get("/", (req, resp) -> {
            Web.message("SQL Is Awesome");
            return Web.renderTemplate("templates/index.vm");
        });

        /* ========================================================================= */
        /* Employee                                                                  */
        /* ========================================================================= */

        /* List All */
        get("/employees", (req, resp) -> {
            List<Employee> employees = Employee.all(1, 10);
            return Web.renderTemplate("templates/employees/index.vm", "employees", employees);
        });

        /* CREATE */
        get("/employees/new", (req, resp) -> {
            Employee employee = new Employee();
            return Web.renderTemplate("templates/employees/new.vm", "employee", employee);
        });

        post("/employees/new", (req, resp) -> {
            Employee emp = new Employee();
            Web.putValuesInto(emp, "FirstName", "LastName");
            if (emp.create()) {
                Web.message("Created An Employee!");
                resp.redirect("/employees/" + emp.getEmployeeId());
                return "";
            } else {
                Web.message("Could Not Create An Employee!");
                return Web.renderTemplate("templates/employees/new.vm", "employee", emp);
            }
        });

        /* READ */
        get("/employees/:id", (req, resp) -> Web.renderTemplate("templates/employees/show.vm",
                "employee", Employee.find(Integer.parseInt(req.params(":id")))));

        /* UPDATE */
        get("/employees/:id/edit", (req, resp) -> {
            Employee employee = Employee.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/employees/edit.vm", "employee", employee);
        });

        post("/employees/:id", (req, resp) -> {
            Employee emp = Employee.find(Integer.parseInt(req.params(":id")));
            Web.putValuesInto(emp, "FirstName", "LastName");
            if (emp.update()) {
                Web.message("Updated Employee!");
                resp.redirect("/employees/" + emp.getEmployeeId());
                return "";
            } else {
                Web.message("Could Not Update Employee!");
                return Web.renderTemplate("templates/employees/edit.vm", "employee", emp);
            }
        });

        /* DELETE */
        get("/employees/:id/delete", (req, resp) -> {
            Employee employee = Employee.find(Integer.parseInt(req.params(":id")));
            employee.delete();
            Web.message("Deleted Employee " + employee.getEmail());
            resp.redirect("/employees");
            return "";
        });
    }

}