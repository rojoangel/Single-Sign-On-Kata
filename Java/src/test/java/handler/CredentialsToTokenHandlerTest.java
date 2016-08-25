package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import service.CredentialsRequest;
import service.Service;
import sso.SSOToken;
import sso.SingleSignOnRegistry;

public class CredentialsToTokenHandlerTest {

    private Mockery context;
    private SingleSignOnRegistry registry;
    private Service service;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        registry = context.mock(SingleSignOnRegistry.class);
        service = context.mock(Service.class);
    }

    @Test
    public void convertsCredentialsToToken() throws Exception {
        String username = "userName";
        String password = "password";
        CredentialsRequest request = new CredentialsRequest("World", null);
        request.setCredentials(username, password);

        SSOToken token = new SSOToken();

        context.checking(new Expectations() {{
            oneOf(registry).register_new_session(username, password);
            will(returnValue(token));

            oneOf(service).handleRequest(new CredentialsRequest(request.getName(), token));
        }});

        CredentialsToTokenHandler handler = new CredentialsToTokenHandler(service, registry);
        handler.handleRequest(request);

        context.assertIsSatisfied();
    }

    @Test
    public void whenTokenIsPresentDelegatesToService() throws Exception {

        CredentialsRequest request = new CredentialsRequest("World", new SSOToken());

        context.checking(new Expectations() {{
            never(registry);

            oneOf(service).handleRequest(request);
        }});

        CredentialsToTokenHandler handler = new CredentialsToTokenHandler(service, registry);
        handler.handleRequest(request);

        context.assertIsSatisfied();
    }

}