package org.eck.jgr;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class BeforeServiceEntry {
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
