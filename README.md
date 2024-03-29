![Build Status](https://github.com/AndrewPleskanko/ExpandAPIs_TestTask/actions/workflows/maven.yml/badge.svg)

### Technologies Used

Before you start, make sure to configure in VM options section for the project, add the following command line arguments
to define configuration parameters:

```
-DDB_USERNAME=your_username 
-DDB_PASSWORD=your_password 
-DDB_HOST=your_database_host 
-DDB_NAME=your_database_name
-DJWT_EXPIRATION=your_jwt_expiration
-Dspring.mail.username=username
-Dspring.mail.password="password"
-Dspring.mail.host=smtp.gmail.com
-Dspring.mail.port=587
-Dspring.mail.properties.mail.smtp.auth=true
-Dspring.mail.properties.mail.smtp.starttls.enable=true
```

- **Spring Boot**: Used for developing the web application based on Java.
- **Spring Security**: Configured to secure the application and authenticate users.
- **Spring Data JPA**: Utilized for database interaction using JPA.
- **Spring MVC**: A framework for building web applications in Java. It provides the model-view-controller architecture.
- **Testing Framework (JUnit and Mockito)**: Utilized for writing and running unit tests, along with creating mock
  objects for comprehensive testing.
- **PostgreSQL**: Used as the relational database management system (RDBMS) for data storage and retrieval in the
  application.
- **Logback (SLF4J)**: A logging framework that provides the ability to log various events in your application.
- **GitHub CI**: Automates continuous integration in GitHub with a configuration file (maven.yml).
- **Liquibase**: Implemented for database schema version control and managing database migrations. Enables seamless
  coordination of database changes across different environments.
- **Swagger**: Integrated for automatic API documentation generation. Simplifies API exploration, testing, and
  understanding with interactive documentation.
- **JavaDoc**: Utilized for in-code documentation to provide comprehensive insights into classes, methods, and packages.
  Enhances code readability and facilitates better collaboration among developers.
- **CheckStyle**: A static code analysis tool that checks code compliance with specified formatting standards. It helps ensure code consistency and clarity. 
- **PMD**: A static code analysis tool that detects potential issues and errors in Java code. It helps identify programming flaws and inefficiencies.
- **Jacoco**: Library for measuring code coverage during software testing. It provides detailed reports on which parts of the code are executed by tests, enabling developers to ensure comprehensive test coverage and identify areas for improvement.
- **Docker(Test container)**: A platform for developing, shipping, and running applications in containers. Test containers provide a lightweight, portable environment for running tests, ensuring consistent and reproducible testing environments.