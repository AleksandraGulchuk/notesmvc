package com.hillel.notesapp.infrastructure;

import com.hillel.notesapp.infrastructure.annotations.*;
import com.hillel.notesapp.infrastructure.reflection.PackageScanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private final ApplicationContext context = new ApplicationContext();
    private final List<Class<?>> controllers;

    public DispatcherServlet() {
        this.controllers = new PackageScanner()
                .findClassesWithAnnotation(Controller.class, "com.hillel.notesapp");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        for (Class<?> controller : controllers) {
            for (Method method : controller.getDeclaredMethods()) {
                String uriFromMethod = getUriFromMethod(req, method);
                if (uriFromMethod == null) {
                    continue;
                }
                String address = req.getContextPath() + "/" + uriFromMethod;
                if (address.equalsIgnoreCase(req.getRequestURI())) {
                    invokeController(req, resp, controller, method);
                    return;
                }
            }
        }
        resp.setStatus(404);
        resp.getWriter().write("NOT FOUND");
    }

    private void invokeController(HttpServletRequest req, HttpServletResponse resp, Class<?> controller, Method method) {
        Object instance = context.getBeanByType(controller);
        try {
            method.invoke(instance, req, resp);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String getUriFromMethod(HttpServletRequest req, Method method) {
        if (req.getMethod().equalsIgnoreCase("get")
                && method.isAnnotationPresent(GetMapping.class)) {
            String annotationValue = method.getAnnotation(GetMapping.class).value();
            if (annotationValue.equalsIgnoreCase("notes") && req.getParameter("id") != null) {
                req.setAttribute("id", req.getParameter("id"));
            }
            return annotationValue;
        }
        if (req.getMethod().equalsIgnoreCase("post")
                && method.isAnnotationPresent(PostMapping.class)) {
            return method.getAnnotation(PostMapping.class).value();
        }
        if (req.getMethod().equalsIgnoreCase("delete")
                && method.isAnnotationPresent(DeleteMapping.class)) {
            return method.getAnnotation(DeleteMapping.class).value();
        }
        if (req.getMethod().equalsIgnoreCase("put")
                && method.isAnnotationPresent(PutMapping.class)) {
            return method.getAnnotation(PutMapping.class).value();
        }
        return null;
    }
}
