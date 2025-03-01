package tools;

import models.User;
import java.sql.Timestamp;

public final class UserSession {
    private static UserSession instance;
    private User currentUser;

    // Private constructor to prevent direct instantiation
    private UserSession() {}

    // Get the singleton instance (no parameters)
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Update user data
    public void setUser(int userId, String firstName, String lastName, String phone,
                        String email, String password, User.Role role) {
        this.currentUser = new User(userId, firstName, lastName, phone, email, password, role);
    }

    // Get the current user
    public User getUser() {
        return currentUser;
    }

    // Clear the session (e.g., on logout)
    public void cleanUserSession() {
        instance = null;
    }
}