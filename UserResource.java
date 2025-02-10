Here's the modified code with the suggested improvements:
```java
import javax.validation.ConstraintViolationException;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class UserResource {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userDao.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Response.ok()
                .entity(user)
                .build();
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("UserNotFound"))
                .build();
        }
    }

    public UserDto createUser(UserDto userDto) {
        ConstraintViolationException exception = new ConstraintViolationException(validator.validate(userDto));
        throw exception;
    }

    public void updateUser(String id, UserDto userDto) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setCreateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            jobDao.update(user);
            return;
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("UserNotFound"))
                .build();
        }
    }

    public void deleteUser(String id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            jobDao.delete(userOptional.get());
            return;
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("UserNotFound"))
                .build();
        }
    }

    public Response<JobEventDto> createJobEvent(String userId, JobEventDto jobEventDto) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            jobEventDto.setUserId(user);
            JobEvent jobEvent = new JobEvent();
            jobEvent.setName(jobEventDto.getName());
            jobEvent.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            jobEventDao.create(jobEvent);
            JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
            responseBuilder.add("status", "ok");
            return Response.ok()
                .entity(responseBuilder.build())
                .build();
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("UserNotFound"))
                .build();
        }
    }

    public Response<Object> updateJobEvent(String jobEventId, JobEventDto jobEventDto) {
        Optional<JobEvent> jobEventOptional = jobEventDao.findById(jobEventId);
        if (jobEventOptional.isPresent()) {
            JobEvent jobEvent = jobEventOptional.get();
            jobEvent.setName(jobEventDto.getName());
            jobEvent.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            jobEventDao.update(jobEvent);
            return Response.ok()
                .build();
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("JobEventNotFound"))
                .build();
        }
    }

    public Response<Object> deleteJobEvent(String jobEventId) {
        Optional<JobEvent> jobEventOptional = jobEventDao.findById(jobEventId);
        if (jobEventOptional.isPresent()) {
            jobEventDao.delete(jobEventOptional.get());
            return Response.ok()
                .build();
        } else {
            throw new WebApplicationException(Response.status
```