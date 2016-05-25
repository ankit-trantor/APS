package application;

import es.esy.chhg.chatapp.data.User;

public class Application {

    private static User mUser;

    public static User getUser() {
        return mUser;
    }

    public static void setUser(User user) {
        mUser = user;
    }
}