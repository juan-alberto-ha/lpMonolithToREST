# Convert an App to RESTful API

## Running starter application

### Changes to starter application code to make it work with Java 17 and newer versions of dependencies
#### pom.xml
- Spring Boot Starter Parent version updated:
```xml
<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.2</version>
```

- Java version updated to 17:
```xml
<properties>
    <java.version>17</java.version>
</properties>
```

- Lombok excluded from Spring Boot plugin:

```xml
<artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>

```
- Added Jackson dependencies

#### Changes to code
- Match.java : Change javax.persistence to jakarta.persistence
- Change thymeleaf syntax :~{fragments/footer.html}

