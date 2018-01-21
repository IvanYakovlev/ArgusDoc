package authorizedUser;

import model.Employee;

public class AuthorizedUser {
    public static Employee user;

    public static Employee getUser() {
        return user;
    }

    public static void setUser(Employee user) {
        AuthorizedUser.user = user;
    }
}
