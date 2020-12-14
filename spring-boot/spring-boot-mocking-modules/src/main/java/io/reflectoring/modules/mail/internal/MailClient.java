package io.reflectoring.modules.mail.internal;

import javax.annotation.PostConstruct;

/**
 * Internal mail server connection. This is not part of the API of the `mail` component.
 */
class MailClient {

    private final boolean connectToMailServer;

    public MailClient(boolean connectToMailServer) {
        this.connectToMailServer = connectToMailServer;
    }

    void sendMail() {

    }

    /**
     * There are many cases where a Spring application connects to external resources during startup, for example when
     * initializing a connection pool to a database or some other service. This method just simulates a failing connection
     * during startup. This forces us not to load this bean during integration tests.
     */
    @PostConstruct
    void connectToMailServer() {
        if(connectToMailServer) {
            throw new RuntimeException("Could not connect to mail server!");
        }
    }

}
