package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import service.Service;
import sso.AuthenticationGateway;
import sso.Request;
import sso.Response;

import static org.junit.Assert.assertEquals;

public class CredentialsHandlerTest {

    @Test
    public void invalidCredentialsAreRejected() throws Exception {
        String username = "invalidUserName";
        String password = "invalidPassword";
        Request request = new Request("World", null);
        request.setCredentials(username, password);

        Mockery context = new Mockery();
        Service service = context.mock(Service.class);
        AuthenticationGateway gateway = context.mock(AuthenticationGateway.class);
        context.checking(new Expectations() {{
            never(service);

            oneOf(gateway).credentialsAreValid(username, password);
            will(returnValue(false));
        }});

        CredentialsHandler handler = new CredentialsHandler(service, gateway);
        Response response = handler.handleRequest(request);

        context.assertIsSatisfied();
        assertEquals("Invalid credentials provided", response.getText());
    }
}
