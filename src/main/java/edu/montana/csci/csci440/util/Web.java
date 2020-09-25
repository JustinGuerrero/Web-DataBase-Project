package edu.montana.csci.csci440.util;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

public class Web {

    public static final int PAGE_SIZE = 10;
    private static Web INSTANCE = new Web();
    static ThreadLocal<Request> REQ = new ThreadLocal<>();
    static ThreadLocal<Response> RESP = new ThreadLocal<>();
    static ThreadLocal<Long> TIMESTAMP = new ThreadLocal<>();

    public static void set(Request request, Response response, long startTime) {
        REQ.set(request);
        RESP.set(response);
        TIMESTAMP.set(startTime);
    }

    public static Request getRequest(){
        return REQ.get();
    }
    public static Response getResponse(){ return RESP.get(); }

    public static String renderTemplate(String index, Object... args) {
        HashMap<Object, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (++i < args.length) {
                map.put(arg, args[i]);
            }
        }
        map.put("message", getMessage());
        map.put("error", getError());
        map.put("web", INSTANCE);
        return new VelocityTemplateEngine().render(new ModelAndView(map, index));
    }

    public static void putValuesInto(Object obj, String... properties) {
        Request req = getRequest();
        try {
            Class<?> clazz = obj.getClass();
            for (String property : properties) {
                Method method = findMethod(clazz, "set" + property);
                if (method.getParameterTypes()[0] == Integer.class || method.getParameterTypes()[0] == Integer.TYPE) {
                    int i = Integer.parseInt(req.queryParams(property));
                    method.invoke(obj, i);
                }
                if (method.getParameterTypes()[0] == Date.class) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = formatter.parse(req.queryParams(property));
                    method.invoke(obj, date);
                }
                if (method.getParameterTypes()[0] == String.class) {
                    method.invoke(obj, req.queryParams(property));
                }
                if (method.getParameterTypes()[0] == BigDecimal.class) {
                    method.invoke(obj, parseBigDecimal(req, property));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static BigDecimal parseBigDecimal(Request req, String property) {
        try {
            return new BigDecimal(req.queryParams(property));
        } catch (Exception e) {
            // formatting exception, return null
            return null;
        }
    }

    private static Method findMethod(Class<?> clazz, String s) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(s)) {
                return method;
            }
        }
        throw new IllegalStateException("Unanable to find a method named " + s);
    }

    public static void message(String s) {
        getRequest().session().attribute(":message", s);
    }

    public static String getMessage() {
        Session session = getRequest().session();
        String message = session.attribute(":message");
        session.removeAttribute(":message");
        return message;
    }

    public static void error(String s) {
        getRequest().session().attribute(":error", s);
    }

    public static String getError() {
        Session session = getRequest().session();
        String message = session.attribute(":error");
        session.removeAttribute(":error");
        return message;
    }

    public static Object redirect(String location) {
        getResponse().redirect(location);
        return "";
    }

    public static Integer integerOrNull(String paramName) {
        return integerOr(paramName, null);
    }

    private static Integer integerOr(String paramName, Integer defaultValue) {
        String val = getRequest().queryParams(paramName);
        if (val != null && !val.equals("")) {
            return Integer.parseInt(val);
        } else {
            return defaultValue;
        }
    }

    public String pagingWidget(List collection) {
        String div = "<div style='padding-bottom:12px'>";
        String prev = prevPage();
        String next = nextPage(collection);
        if (prev.equals("")) {
            div += next;
        } else {
            div += prev + " &#9679; Page " + getPage() + " &#9679; " + next;
        }
        return div + "</div>";
    }

    public String nextPage(List collection){
        if (collection.size() == PAGE_SIZE) {
            Integer page = getPage();
            return "<a href='" + getRequest().pathInfo() + "?page=" + (page + 1) + "'>Next Page &gt;&gt;</a>";
        } else {
            return "";
        }
    }

    public static Integer getPage(){
        String page = getRequest().queryParams("page");
        if (page != null) {
            return Integer.parseInt(page);
        } else {
            return 1;
        }
    }

    public String prevPage() {
        Integer page = getPage();
        if (page > 2) {
            return "<a href='" + getRequest().pathInfo() + "?page=" + (page - 1) + "'>&lt;&lt;  Previous Page</a>";
        } else if (page == 2) {
            return "<a href='" + getRequest().pathInfo() + "'>&lt;&lt;  Previous Page</a>";
        } else {
            return "";
        }
    }

    public String select(String model, String displayProperty, Integer selected) throws Exception {
        return select(model, displayProperty, selected, false);
    }

    public String select(String model, String displayProperty, Object selectedId, boolean includeEmpty) throws Exception {
        String select = "<select style='max-width:200px' name='" + model + "Id'>\n";
        Class<?> clazz = Class.forName("edu.montana.csci.csci440.model." + model);
        Method all = clazz.getMethod("all");
        List invoke = (List) all.invoke(null);
        Method idGetter = clazz.getMethod("get" + model + "Id");
        Method displayGetter = clazz.getMethod("get" + displayProperty);
        if (includeEmpty) {
            select += "<option></option>";
        }
        for (Object o : invoke) {
            Object idValue = idGetter.invoke(o);
            String selectedString;
            if (idValue != null &&
                    selectedId != null &&
                    idValue.toString().equals(selectedId.toString())) {
                selectedString = " selected";
            } else {
                selectedString = "";
            }
            select += "  <option value='" + idValue + "' " + selectedString + ">" +
                    displayGetter.invoke(o) +
                    "</option>\n";
        }
        select += "</select>\n";
        return select;
    }

    public String param(String name) {
        return getRequest().queryParams(name);
    }

    public static void init() {
        before((request, response) -> {
            System.out.println(">> REQUEST " + request.requestMethod() + " " + request.pathInfo());
            Web.set(request, response, System.currentTimeMillis());
        });
        after((request, response) -> {
            Long aLong = TIMESTAMP.get();
            System.out.println("  << REQUEST " + request.requestMethod() + " " + request.pathInfo() + " completed in " + ((System.currentTimeMillis() - TIMESTAMP.get()) / 1000.0) + " seconds");
        });

        exception(Exception.class, (e, request, response) -> {
            System.out.println("################################################################");
            System.out.println("#  ERROR ");
            System.out.println("################################################################");
            System.out.println("An error occured: " + e.getMessage());
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            response.status(500);
            response.body(Web.renderTemplate("templates/error.vm",
                    "error", e,
                    "stacktrace", sw.getBuffer().toString()));
        });
    }

}
