package service;

import sso.Credentials;
import sso.Request;
import sso.SSOToken;

public class CredentialsRequest extends Request {
    private Credentials credentials;

    public CredentialsRequest(String name, SSOToken token) {
        super(name, token);
    }

    public void setCredentials(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CredentialsRequest that = (CredentialsRequest) o;

        return credentials != null ? credentials.equals(that.credentials) : that.credentials == null;

    }

    @Override
    public int hashCode() {
        return credentials != null ? credentials.hashCode() : 0;
    }
}
