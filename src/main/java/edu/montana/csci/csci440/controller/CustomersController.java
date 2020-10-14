package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Artist;
import edu.montana.csci.csci440.model.Customer;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class CustomersController {
    public static void init(){
        /* CREATE */
        get("/customers/new", (req, resp) -> {
            Customer customer = new Customer();
            return Web.renderTemplate("templates/customers/new.vm", "customer", customer);
        });

        post("/customer/new", (req, resp) -> {
            Customer customer = new Customer();
            Web.putValuesInto(customer, "FirstName", "LastName");
            if (customer.create()) {
                Web.message("Created A Customer!");
                return Web.redirect("/customers/" + customer.getCustomerId());
            } else {
                Web.error("Could Not Create An Customer!");
                return Web.renderTemplate("templates/customers/new.vm",
                        "customer", customer);
            }
        });
        /* READ */
        get("/customers", (req, resp) -> {
            List<Customer> customers = Customer.all(Web.getPage(), Web.PAGE_SIZE);
            return Web.renderTemplate("templates/customers/index.vm",
                    "customers", customers);
        });

        get("/customers/:id", (req, resp) -> {
            Customer customer = Customer.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/customers/show.vm",
                    "customer", customer);
        });
        /* UPDATE */
        get("/customers/:id/edit", (req, resp) -> {
            Customer customer = Customer.find(Integer.parseInt(req.params(":id")));
            Web.putValuesInto(customer,"FirstName", "LastName");
            return Web.renderTemplate("templates/customers/edit.vm",
                    "customer", customer);
        });

        post("/customers/:id", (req, resp) -> {
            Customer customer = Customer.find(Integer.parseInt(req.params(":id")));
            Web.putValuesInto(customer, "FirstName", "LastName");
            if (customer.update()) {
                Web.message("Updated Artist!");
                return Web.redirect("/customer/" + customer.getCustomerId());
            } else {
                Web.error("Could Not Update Customer!");
                return Web.renderTemplate("templates/customers/edit.vm",
                        "customer", customer);
            }
        });

        /* DELETE */
        get("/artists/:id/delete", (req, resp) -> {
            Customer customer = Customer.find(Integer.parseInt(req.params(":id")));
            customer.delete();
            Web.message("Deleted customer " + customer.getCustomerId());
            return Web.redirect("/customer");
        });
    }
    }

