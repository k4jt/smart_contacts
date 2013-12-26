package ua.kpi.sc.model;

/**
 * Created by manilo on 23.11.13.
 */
public class User {

    private long id;

    private String firstName;

    private String lastName;

    private String login;

    private String email;

    private long passwordHash;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPassword() {
        return passwordHash;
    }

    public void setPassword(int password) {
        passwordHash = password;
    }

    public void setPassword(String password) {
        passwordHash = password.hashCode();
    }

}
