package org.eck.jgr;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eck.jgr.annotations.BeforeService;
import org.eck.jgr.annotations.JGR;
import org.eck.jgr.annotations.Service;
import org.eck.jgr.path.RouteParser;
import org.reflections.Reflections;

public class JGRServlet extends HttpServlet {
    private static final long serialVersionUID = -3682253696287441567L;

    private String prefix;
    private String servicesPackage;

    private Map<String, Map<String, ServiceEntry>> services = new HashMap<String, Map<String, ServiceEntry>>();
    private Map<String, BeforeServiceEntry> befores = new HashMap<String, JGRServlet.BeforeServiceEntry>();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.prefix = getServletConfig().getInitParameter("prefix");
        this.servicesPackage = getServletConfig().getInitParameter("servicesPackage");

        readClasspath();
    }

    private void readClasspath() {
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
                        if (services.get(httpMethod) == null) {
                            services.put(httpMethod, new HashMap<String, ServiceEntry>());
                        }

                        String path = s.path();
                        ServiceEntry se = new ServiceEntry();
                        se.setClazz(clazz);
                        se.setMethod(method);
                        se.setBefore(s.before());
                        services.get(httpMethod).put(path, se);
                    } else if (annotation instanceof BeforeService) {
                        BeforeService beforeService = (BeforeService) annotation;
                        BeforeServiceEntry entry = new BeforeServiceEntry();
                        entry.setClazz(clazz);
                        entry.setMethod(method);
                        befores.put(beforeService.name(), entry);
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @SuppressWarnings("rawtypes")
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        if (this.prefix != null) {
            url = url.replace(this.prefix, "");
        }
        String httpMethod = req.getMethod().toUpperCase();
        Map<String, ServiceEntry> services = this.services.get(httpMethod);

        if (services != null) {
            Set<Entry<String, ServiceEntry>> entrySet = services.entrySet();
            for (Entry<String, ServiceEntry> entry : entrySet) {
                if (RouteParser.macthes(entry.getKey(), url)) {

                    Params params = new Params();

                    Enumeration parameterNames = req.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String name = (String) parameterNames.nextElement();
                        params.addMultiValue(name, req.getParameterValues(name));
                    }

                    Map<String, String> parse = RouteParser.parse(entry.getKey(), url);
                    Set<Entry<String, String>> routeParams = parse.entrySet();
                    for (Entry<String, String> routeParam : routeParams) {
                        params.addSingleValue(routeParam.getKey(), routeParam.getValue());
                    }

                    try {
                        ServiceEntry se = entry.getValue();
                        if (se.getBefore() != null) {
                            for (String s : se.getBefore()) {
                                BeforeServiceEntry beforeServiceEntry = befores.get(s);
                                Object newInstance = beforeServiceEntry.getClazz().newInstance();
                                Boolean result = (Boolean) beforeServiceEntry.getMethod().invoke(newInstance, req, resp, params);
                                if (!result) {
                                    return;
                                }
                            }
                        }
                        Object newInstance = se.getClazz().newInstance();
                        Method method = se.getMethod();
                        method.invoke(newInstance, req, resp, params);
                        return;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        resp.sendError(404);
    }

    @SuppressWarnings("rawtypes")
    class BeforeServiceEntry {
        private Class clazz;
        private Method method;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }

    @SuppressWarnings("rawtypes")
    class ServiceEntry {
        private Class clazz;
        private Method method;
        private String[] before;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public String[] getBefore() {
            return before;
        }

        public void setBefore(String[] before) {
            this.before = before;
        }
    }
}
