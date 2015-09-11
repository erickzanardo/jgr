package org.eck.jgr;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ServiceEntry {
    private Class clazz;
    private Method method;
    private String[] before;
    private Map<String, String> paramsDescriptions = new HashMap<String, String>();

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

    public Map<String, String> getParamsDescriptions() {
        return paramsDescriptions;
    }
}
