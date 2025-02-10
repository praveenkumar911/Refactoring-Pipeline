Here are some possible changes to improve the readability and maintainability of the code:

1. Extract methods for repeated code:

The code contains several blocks of code that are repeated with minor differences. These blocks of code can be extracted into separate methods to improve readability and maintainability.

For example, the following block of code is repeated three times with minor differences:
```java
if (user == null) {
    throw new ClientException("UserNotFound", "The user doesn't exist");
}
```
This block of code can be extracted into a separate method called `checkUserExists`:
```java
private void checkUserExists(User user) {
    if (user == null) {
        throw new ClientException("UserNotFound", "The user doesn't exist");
    }
}
```
This method can be called in the places where the original block of code was used.

2. Use try-with-resources to close resources:

The code uses the `new` keyword to create objects that implement the `AutoCloseable` interface, such as `UserDao`, `JobDao`, and `JobEventDao`. These objects need to be closed after they are used.

The try-with-resources statement can be used to ensure that these objects are closed automatically. For example:
```java
try (UserDao userDao = new UserDao()) {
    User user = userDao.getActiveByUsername(username);
    checkUserExists(user);
    // use user object here
}
```
3. Use Java 8 features:

The code uses the `JSONObject` class from the `org.codehaus.jettison.json` package to create JSON objects. Java 8 introduced the `JSONObject` class in the `javax.json.json` package.

The Java 8 `JSONObject` class has several advantages over the `org.codehaus.jettison.json` `JSONObject` class. For example, it supports method chaining, which can make the code more readable.

The Java 8 `JSONObject` class can be used as follows:
```java
import javax.json.Json;
import javax.json.JsonObjectBuilder;

// ...

JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
responseBuilder.add("status", "ok");
JsonObject response = responseBuilder.build();
return Response.ok().entity(response).build();
```
4. Use `java.time` package instead of `java.util.Date`:

The code uses the `java.util.Date` class to represent dates and times. The `java.time` package, introduced in Java 8, provides a more modern and flexible way to represent dates and times.

For example, the following code uses the `LocalDateTime` class from the `java.time` package:
```java
import java.time.LocalDateTime;

// ...

LocalDateTime now = LocalDateTime.now();
user.setCreateDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
```
5. Use `org.hibernate.validator` instead of `com.sismics.util.ValidationUtil`:

The code uses the `ValidationUtil` class to validate user input. The `org.hibernate.validator` package provides a more powerful and flexible way to validate user input.

For example, the following code uses the `@NotBlank` annotation from the `org.hibernate.validator` package:
```java
import javax.validation.constraints.NotBlank;

// ...

public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    // getters and setters
}

// ...

UserDto userDto = new UserDto();
userDto.setUsername(username);
userDto.setPassword(password);
userDto.setEmail(email);

Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
if (!violations.isEmpty()) {
    throw new ConstraintViolationException(violations);
}
```
6. Use `java.util.Optional` instead of null checks:

The code uses null checks to handle missing values. The `java.util.Optional` class, introduced in Java 8, provides a more elegant way to handle missing values.

For example, the following code uses the `Optional` class:
```java
import java.util.Optional;

// ...

public class UserDao {

    // ...

    public Optional<User> findByUsername(String username) {
        // query here
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}

// ...

Optional<User> userOptional = userDao.findByUsername(username);
if (userOptional.isEmpty()) {
    throw new ClientException("UserNotFound", "The user doesn't exist");
}
User user = userOptional.get();
```
7. Use interfaces instead of concrete classes:

The code uses concrete classes for database access objects (DAOs) and data transfer objects (DTOs). Using interfaces instead of concrete classes can improve flexibility and testability.

For example, the following code defines interfaces for the `UserDao` and `UserDto` classes:
```java
// UserDao.java
import javax.persistence.EntityManager;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    String create
```