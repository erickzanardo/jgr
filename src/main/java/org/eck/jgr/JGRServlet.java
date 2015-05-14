package org.eck.jgr;

import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.eck.jgr.annotations.JGR;
import org.reflections.Reflections;

public class JGRServlet extends HttpServlet {

    private String prefix;
    private String servicesPackage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.prefix = getServletConfig().getInitParameter("prefix");
        this.servicesPackage = getServletConfig().getInitParameter("servicesPackage");

        readClasspath();
    }

    private void readClasspath() {
        Reflections reflections = new Reflections(servicesPackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(JGR.class);
    }
    
}
