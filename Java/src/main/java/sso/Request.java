package sso;

public class Request {

    private String name;
    private SSOToken token;
    private Credentials credentials;

    public Request(String name, SSOToken token) {
        this.name = name;
        this.token = token;
    }
    
    public SSOToken getSSOToken() {
        return this.token;
    }
    
    public String getName() {
        return this.name;
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

        Request request = (Request) o;

        if (name != null ? !name.equals(request.name) : request.name != null) return false;
        if (token != null ? !token.equals(request.token) : request.token != null) return false;
        return credentials != null ? credentials.equals(request.credentials) : request.credentials == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        return result;
    }
}
