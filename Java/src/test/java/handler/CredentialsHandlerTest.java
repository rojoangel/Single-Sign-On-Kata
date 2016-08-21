package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import service.Service;
import sso.*;

import static org.junit.Assert.assertEquals;

public class CredentialsHandlerTest {
    private Mockery context;
    private Service service;
    private AuthenticationGateway gateway;
    private SingleSignOnRegistry registry;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        service = context.mock(Service.class);
        gateway = context.mock(AuthenticationGateway.class);
        registry = context.mock(SingleSignOnRegistry.class);
    }

    @Test
    public void invalidCredentialsAreRejected() throws Exception {
        String username = "invalidUserName";
        String password = "invalidPassword";
        Request request = new Request("World", null);
        request.setCredentials(username, password);

        context.checking(new Expectations() {{
            never(service);

            never(registry);

            oneOf(gateway).credentialsAreValid(username, password);
            will(returnValue(false));
        }});

        CredentialsHandler handler = new CredentialsHandler(service, gateway, registry);
        Response response = handler.handleRequest(request);

        context.assertIsSatisfied();
        assertEquals("Invalid credentials provided", response.getText());
    }

    @Test
    public void validCredentialsAreAccepted() throws Exception {
        String username = "validUserName";
        String password = "validPassword";
        Request request = new Request("World", null);
        request.setCredentials(username, password);
        SSOToken token = new SSOToken();

        context.checking(new Expectations() {{
            oneOf(gateway).credentialsAreValid(username, password);
            will(returnValue(true));

            oneOf(registry).register_new_session(username, password);
            will(returnValue(token));

            oneOf(service).handleRequest(new Request("World", token));
        }});

        CredentialsHandler handler = new CredentialsHandler(service, gateway, registry);
        handler.handleRequest(request);

        context.assertIsSatisfied();
    }
}