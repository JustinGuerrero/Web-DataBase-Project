package edu.montana.csci.csci440;

import edu.montana.csci.csci440.model.Employee;
import edu.montana.csci.csci440.util.Web;

import static spark.Spark.*;

class Server {

    public static void main(String[] args) {
        get("/", (req, resp) -> Web.renderTemplate("templates/index.vm",
                "message", "SQL Is Awesome!",
                "employees", Employee.all(1, 10)));
    }

}