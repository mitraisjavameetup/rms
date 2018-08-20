# What's different from Astri
My study case focus on back end pattern, therefore it's so little changes on front-end (only CRUD for user).
I use DI pattern for database connection instead of Singleton.

# RMS
Study case project using various technology stack for building Resource Management System

## Overview
The application is using maven multimodule where each module will provides the same functionalities with different technology stack.

```
rms (parent project)
|-- src
|  	|-- main
|      	|-- docker (docker related configuration e.g. database server)
|      	|-- sql (database scripts, sample data)
		|-- sqlite (sqlite database file)
|-- rms-servlet-web (Servlet, JSP, JDBC technology stack)
|  	|-- src
|  	|   |-- main
|  	|       |-- java (java source file)
|	|		|	|-- com.mitrais.rms.connection (database connection related class)
|	|		|	|-- com.mitrais.rms.controller (servlet controller class)
|	|		|	|-- com.mitrais.rms.dao (data access related interface)	
|	|		|	|-- com.mitrais.rms.dao.impl (implementation of dao interface)
|	|		|	|-- com.mitrais.rms.injectorbinding (google guice injector module)
|	|		|	|-- com.mitrais.rms.model (data class)
|  	|       |-- resources (configuration, properties)
|  	|       |-- webapp (web specific files, css, js, jsp, html)
|  	|-- pom.xml
|-- pom.xml
```

## rms-servlet-web
It is implementing MVC pattern using only Servlet and JSP, combine with plain JDBC to handle database operation.

It uses tomcat7-maven-plugin to spin up embedded tomcat 7, therefore no need to install tomcat 7 on your local machine.
It uses sqlite as database, therefore no need to have any run sql machine.

To run the application, execute maven command `mvn tomcat7:run` and browse http://localhost:8080/rms-servlet-web/index.jsp
