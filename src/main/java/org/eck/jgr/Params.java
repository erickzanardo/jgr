package org.eck.jgr;

import java.util.HashMap;
import java.util.Map;

public class Params {
    private Map<String, Param> params = new HashMap<String, Param>();

    public Param get(String key) {
        return params.get(key);
    }

    public void addSingleValue(String key, Object value) {
        params.put(key, new Param(new Object[] { value }));
    }

    public void addMultiValue(String key, Object[] value) {
        params.put(key, new Param(value));
    }

}
