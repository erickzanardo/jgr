# JGR
Just Gimme Routes is a simple extensions for the native java sevlet API, it implements simple features that can't be achieved with only the provided classes by the servlet api.

# Configuration

To use JGR you must declare it's servlet on your web.xml

```xml
	<servlet>
		<servlet-name>JGR</servlet-name>
		<servlet-class>org.eck.jgr.JGRServlet</servlet-class>
		<init-param>
			<param-name>servicesPackage</param-name>
			<param-value>com.services</param-value>
		</init-param>
		<init-param>
			<param-name>prefix</param-name>
			<param-value>/services</param-value>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>JGR</servlet-name>
		<url-pattern>/services/*</url-pattern>
	</servlet-mapping>
```
### servicesPackage
This param tells JGR where to find your annotated classes

### prefix
This tells JGR what is the url-pattern you used to map the JGR servlet

# Features

# Routes

To create a route you just need to annotate a class with the @JGR, this will tel JGR that this class contains services, and then annotate methods of this class with @Service, see an example below

```java
@JGR
public class AdminService {

  @Service(method = "GET", path = "/admin/purchases")
  public void purchases(HttpServletRequest req, HttpServletResponse resp, Params params) throws IOException {
  }
}
```

# Before Services

Before services are methods that should contain common logic and is executed before services, this is usefull to make generic validation, like verifying if the user is logged, to declare a Before Service is very similar to services, the only difference is that a before service must return a boolean result, this will tell jgr if it should continue on the request chain. See an example below:

```java
@JGR
public class LoggedService {
  @BeforeService(name="verifyLogged")
  public boolean verifyLogged(HttpServletRequest req, HttpServletResponse resp, Params params) throws IOException {
    return Shortcuts.verifyLogged(resp);
  }
}
```

Then it could be used on a service annotation
```java
@JGR
public class AdminService {

  @Service(method = "GET", path = "/admin/purchases", before = { "verifyLogged" })
  public void purchases(HttpServletRequest req, HttpServletResponse resp, Params params) throws IOException {
  }
}
```
