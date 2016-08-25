package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import service.CredentialsRequest;
import service.Service;
import sso.AuthenticationGateway;
import sso.Response;
import sso.SSOToken;

import static org.junit.Assert.assertEquals;

public class CredentialsValidationHandlerTest {

    private Mockery context;
    private Service service;
    private AuthenticationGateway gateway;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        service = context.mock(Service.class);
        gateway = context.mock(AuthenticationGateway.class);
    }

    @Test
    public void invalidCredentialsAreRejected() throws Exception {
        String username = "invalidUserName";
        String password = "invalidPassword";
        CredentialsRequest request = new CredentialsRequest("World", null);
        request.setCredentials(username, password);

        context.checking(new Expectations() {{
            never(service);

            oneOf(gateway).credentialsAreValid(username, password);
            will(returnValue(false));
        }});

        CredentialsValidationHandler handler = new CredentialsValidationHandler(service, gateway);
        Response response = handler.handleRequest(request);

        context.assertIsSatisfied();
        assertEquals("Invalid credentials provided", response.getText());
    }

    @Test
    public void nullCredentialsAndTokenAreRejected() throws Exception {
        CredentialsRequest request = new CredentialsRequest("World", null);

        context.checking(new Expectations() {{
            never(gateway);
            never(service);
        }});

        CredentialsValidationHandler handler = new CredentialsValidationHandler(service, gateway);
        Response response = handler.handleRequest(request);

        assertEquals("No credentials provided, please log in the application", response.getText());
        context.assertIsSatisfied();
    }

    @Test
    public void whenTokenIsPresentDelegatesToService() throws Exception {

        CredentialsRequest request = new CredentialsRequest("World", new SSOToken());

        context.checking(new Expectations() {{
            never(gateway);

            oneOf(service).handleRequest(request);
        }});

        CredentialsValidationHandler handler = new CredentialsValidationHandler(service, gateway);
        handler.handleRequest(request);

        context.assertIsSatisfied();
    }
}
