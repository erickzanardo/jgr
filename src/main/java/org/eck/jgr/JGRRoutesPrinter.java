package org.eck.jgr;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JGRRoutesPrinter {

    public static String routesReport(String servicesPackage) {
        ClasspathReaderResult result = ClasspathReader.readClasspath(servicesPackage);
        StringBuilder str = new StringBuilder();

        str.append("\n").append("Routes").append("\n");
        str.append("--------------------").append("\n");

        Set<Entry<String, Map<String, ServiceEntry>>> methods = result.getServices().entrySet(); 
        for(Entry<String, Map<String, ServiceEntry>> method : methods) {
            str.append(method.getKey()).append("\n");
            str.append("--------------------").append("\n");

            Set<Entry<String, ServiceEntry>> services = method.getValue().entrySet();
            for(Entry<String, ServiceEntry> service : services) {
                str.append("    - ").append(service.getKey()).append("\n");
                if(service.getValue().getParamsDescriptions().size() > 0) {
                    str.append("        (Parameters)").append("\n");
                    Set<Entry<String, String>> params = service.getValue().getParamsDescriptions().entrySet();
                    for(Entry<String, String> param : params) {
                        str.append("        - ").append(param.getKey());
                        if(param.getValue() != null && !"".equals(param.getValue())) {
                            str.append(": ").append(param.getValue());
                        }
                        str.append("\n");
                    }
                }
            }
        }
        
        return str.toString();
    }
}
