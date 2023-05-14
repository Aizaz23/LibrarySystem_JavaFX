package nl.inholland.librarysystemjavafx.model;

public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User(){ }

    public User(String username){ this.username = username; }

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return (firstName + " " + lastName);
    }

}
