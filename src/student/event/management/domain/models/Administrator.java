package student.event.management.domain.models;

import java.util.StringJoiner;

public class Administrator {

    private Long id;

    private String username;

    private String password;

    public static Administrator builder() {
        return new Administrator();
    }

    private Administrator() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Administrator setId(Long id) {
        this.id = id;
        return this;
    }

    public Administrator setUsername(String username) {
        this.username = username;
        return this;
    }

    public Administrator setPassword(String password) {
        this.password = password;
        return this;
    }

    public String toString() {
        return new StringJoiner(", ", Administrator.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .toString();
    }
}
