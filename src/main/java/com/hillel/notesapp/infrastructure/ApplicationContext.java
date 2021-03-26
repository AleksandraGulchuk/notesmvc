package com.hillel.notesapp.infrastructure;

import com.hillel.notesapp.infrastructure.annotations.Autowired;
import com.hillel.notesapp.infrastructure.annotations.Component;
import com.hillel.notesapp.infrastructure.annotations.Controller;
import com.hillel.notesapp.infrastructure.reflection.PackageScanner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext {

    private final Map<Class, Object> beans = new HashMap<>();
    private final PackageScanner packageScanner = new PackageScanner();

    public ApplicationContext() {
        createBeans();
    }

    public Object getBeanByType(Class type) {
        return beans.keySet().stream()
                .filter(type::isAssignableFrom)
                .findFirst()
                .map(cls -> beans.get(cls))
                .orElse(null);
    }

    private void createBeans() {
        List<Class> componentsClasses = getComponentsClasses();
        for (Class componentsClass : componentsClasses) {
            createBean(componentsClass);
        }
        for (Class componentsClass : componentsClasses) {
            postProcessBean(beans.get(componentsClass));
        }
    }

    private void postProcessBean(Object bean) {
        List<Field> fields = Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());
        for (Field field : fields) {
            field.setAccessible(true);
            Class type = field.getType();
            Object value = getBeanByType(type);
            try {
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void createBean(Class componentsClass) {
        try {
            Object object = componentsClass.getConstructor().newInstance();
            beans.put(componentsClass, object);
        } catch (Exception e) {
            throw new RuntimeException("Error create bean " + componentsClass.getSimpleName(), e);
        }
    }

    private List<Class> getComponentsClasses() {
        String packageName = "com.hillel.notesapp";
        List<Class<?>> controllers = packageScanner
                .findClassesWithAnnotation(Controller.class, packageName);
        List<Class<?>> components = packageScanner
                .findClassesWithAnnotation(Component.class, packageName);
        return Stream
                .concat(controllers.stream(), components.stream())
                .collect(Collectors.toList());
    }
}
