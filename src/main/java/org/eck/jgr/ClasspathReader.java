package org.eck.jgr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import org.eck.jgr.annotations.BeforeService;
import org.eck.jgr.annotations.JGR;
import org.eck.jgr.annotations.ParamDescription;
import org.eck.jgr.annotations.Service;
import org.reflections.Reflections;

public class ClasspathReader {
    public static ClasspathReaderResult readClasspath(String servicesPackage) {
        ClasspathReaderResult result = new ClasspathReaderResult();

        Reflections reflections = new Reflections(servicesPackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(JGR.class);

        for (Class<?> clazz : annotated) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    // Services
                    if (annotation instanceof Service) {
                        Service s = (Service) annotation;
                        String httpMethod = s.method();
                        if (result.getServices().get(httpMethod) == null) {
                            result.getServices().put(httpMethod, new HashMap<String, ServiceEntry>());
                        }

                        String path = s.path();
                        ServiceEntry se = new ServiceEntry();
                        se.setClazz(clazz);
                        se.setMethod(method);
                        se.setBefore(s.before());
                        for (Annotation _annotation : annotations) {
                            if(_annotation instanceof ParamDescription) {
                                ParamDescription paramDesc = (ParamDescription) _annotation;
                                se.getParamsDescriptions().put(paramDesc.name(), paramDesc.description());
                            }
                        }
                        result.getServices().get(httpMethod).put(path, se);
                    } else if (annotation instanceof BeforeService) {
                        BeforeService beforeService = (BeforeService) annotation;
                        BeforeServiceEntry entry = new BeforeServiceEntry();
                        entry.setClazz(clazz);
                        entry.setMethod(method);
                        result.getBefores().put(beforeService.name(), entry);
                    }
                }
            }
        }

        return result;
    }
}
