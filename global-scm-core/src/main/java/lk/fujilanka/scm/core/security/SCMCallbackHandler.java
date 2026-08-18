package lk.fujilanka.scm.core.security;

import javax.security.auth.callback.*;
import java.io.IOException;

public class SCMCallbackHandler implements CallbackHandler {
    private final String username;
    private final String password;

    public SCMCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password != null ? password.toCharArray() : new char[0]);
            }
        }
    }
}
