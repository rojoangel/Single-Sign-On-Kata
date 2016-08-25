package acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import service.CredentialsRequest;
import service.Service;
import service.ServiceFactory;
import sso.AuthenticationGateway;
import sso.Response;
import sso.SSOToken;
import sso.SingleSignOnRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AcceptanceTest {

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

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(new CredentialsRequest("Foo", token));

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }

    @Test
    public void nullSSOTokenIsRejected() {
        context.checking(new Expectations() {{
            never(authenticationGateway);
            never(ssoRegistry);
        }});

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(new CredentialsRequest("Foo", null));

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

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(new CredentialsRequest("Foo", token));

        context.assertIsSatisfied();
        assertEquals("hello Foo!", response.getText());
    }

    @Test
    public void invalidCredentialsAreRejected() {

        CredentialsRequest request = new CredentialsRequest("Foo", null);
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
        CredentialsRequest request = new CredentialsRequest("Foo", null);

        context.checking(new Expectations() {{
            never(ssoRegistry);
            never(authenticationGateway);
        }});

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(request);

        context.assertIsSatisfied();
        assertNotEquals("hello Foo!", response.getText());
    }

    @Test
    public void validCredentialsAreAccepted() throws Exception {

        String username = "validUsername";
        String password = "validPassword";
        CredentialsRequest request = new CredentialsRequest("Foo", null);
        request.setCredentials(username, password);

        SSOToken token = new SSOToken();

        context.checking(new Expectations() {{
            oneOf(authenticationGateway).credentialsAreValid(username, password);
            will(returnValue(true));

            oneOf(ssoRegistry).register_new_session(username, password);
            will(returnValue(token));

            oneOf(ssoRegistry).is_valid(token);
            will(returnValue(true));
        }});

        Service service = ServiceFactory.handlingSSOTokenAndCredentials(authenticationGateway, ssoRegistry);
        Response response = service.handleRequest(request);

        context.assertIsSatisfied();
        assertEquals("hello Foo!", response.getText());

    }
}
