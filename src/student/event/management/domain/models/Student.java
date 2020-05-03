package student.event.management.domain.models;

import java.util.StringJoiner;

public class Student {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private boolean hasOrganisationRights;

    private Student () {

    }

    public static Student builder() {
        return new Student();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isHasOrganisationRights() {
        return hasOrganisationRights;
    }

    public Student setId(Long id) {
        this.id = id;
        return this;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Student setUsername(String username) {
        this.username = username;
        return this;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }

    public Student setHasOrganisationRights(boolean hasOrganisationRights) {
        this.hasOrganisationRights = hasOrganisationRights;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("hasOrganisationRights=" + hasOrganisationRights)
                .toString();
    }
}
