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
}
