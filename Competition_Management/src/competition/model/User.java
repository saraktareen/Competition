package competition.model;

//This is to determine the access level of a user
public class User {
    private String role;

    public User(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}