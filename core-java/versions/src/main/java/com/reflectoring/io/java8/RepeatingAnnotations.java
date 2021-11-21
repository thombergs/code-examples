package com.reflectoring.io.java8;

import java.lang.annotation.Repeatable;

public class RepeatingAnnotations {

    @Repeatable(Notifications.class)
    public @interface Notify {
        String email();
    }

    public @interface Notifications {
        Notify[] value();
    }

    @Notify(email = "admin@company.com")
    @Notify(email = "owner@company.com")
    public class UserNotAllowedForThisActionException
            extends RuntimeException {
        final String user;

        public UserNotAllowedForThisActionException(String user) {
            this.user = user;

        }
    }
}
