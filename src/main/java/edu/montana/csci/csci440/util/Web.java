package edu.montana.csci.csci440.util;

import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.velocity.VelocityTemplateEngine;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Web {

    static ThreadLocal<Request> _request = new ThreadLocal<>();

    public static void setReq(Request request) {
        _request.set(request);
    }

    public static Request getRequest(){
        return _request.get();
    }

    public static String renderTemplate(String index, Object... args) {
        HashMap<Object, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (++i < args.length) {
                map.put(arg, args[i]);
            }
        }
        map.put("message", getMessage());
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
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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

}
