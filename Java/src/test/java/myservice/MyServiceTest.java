package myservice;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import service.Service;
import service.ServiceFactory;
import sso.*;

public class MyServiceTest {

    private Mockery context;
    private SingleSignOnRegistry ssoRegistry;
    private AuthenticationGateway authenticationGateway;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        ssoRegistry = context.mock(SingleSignOnRegistry.class);
        authenticationGateway = context.mock(AuthenticationGateway.class);
    }

    @Test
    public void invalidSSOTokenIsRejected() {
        SSOToken token = new SSOToken();
        context.checking(new Expectations() {{
            never(authenticationGateway);

            oneOf(ssoRegistry).is_valid(token);
            will(returnValue(false));
        }});

        Service service = ServiceFactory.handlingSSOToken(ssoRegistry);
        Response response = service.handleRequest(new Request("Foo", token));

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }

    @Test
    public void nullSSOTokenIsRejected() {
        context.checking(new Expectations() {{
            never(authenticationGateway);

            oneOf(ssoRegistry).is_valid(null);
            will(returnValue(false));
        }});

        Service service = ServiceFactory.handlingSSOToken(ssoRegistry);
        Response response = service.handleRequest(new Request("Foo", null));

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }

    @Test
    public void validSSOTokenIsAccepted() {
        SSOToken token = new SSOToken();
        context.checking(new Expectations() {{
            never(authenticationGateway);

            oneOf(ssoRegistry).is_valid(token);
            will(returnValue(true));
        }});

        Service service = ServiceFactory.handlingSSOToken(ssoRegistry);
        Response response = service.handleRequest(new Request("Foo", token));

        context.assertIsSatisfied();
        assertEquals("hello Foo!", response.getText());
    }

    @Test
    public void invalidCredentialsAreRejected() {

        Request request = new Request("Foo", null);
        String username = "invalidUserName";
        String password = "invalidPassword";
        request.setCredentials(username, password);

        context.checking(new Expectations() {{
            never(ssoRegistry);

            oneOf(authenticationGateway).credentialsAreValid(username, password);
            will(returnValue(false));

        }});

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(request);

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }

    @Test
    public void nullCredentialsAreRejected() throws Exception {
        Request request = new Request("Foo", null);

        context.checking(new Expectations() {{
            never(ssoRegistry);
            never(authenticationGateway);
        }});

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(request);

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }
}
