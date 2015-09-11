package org.eck.jgr;

import java.util.HashMap;
import java.util.Map;

public class ClasspathReaderResult {
    private Map<String, Map<String, ServiceEntry>> services = new HashMap<String, Map<String, ServiceEntry>>();
    private Map<String, BeforeServiceEntry> befores = new HashMap<String, BeforeServiceEntry>();

    public Map<String, Map<String, ServiceEntry>> getServices() {
        return services;
    }

    public Map<String, BeforeServiceEntry> getBefores() {
        return befores;
    }
}
