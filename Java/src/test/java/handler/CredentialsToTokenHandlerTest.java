package handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import service.Service;
import sso.Request;
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
        Request request = new Request("World", null);
        request.setCredentials(username, password);

        SSOToken token = new SSOToken();

        context.checking(new Expectations() {{
            oneOf(registry).register_new_session(username, password);
            will(returnValue(token));

            oneOf(service).handleRequest(new Request(request.getName(), token));
        }});

        CredentialsToTokenHandler handler = new CredentialsToTokenHandler(service, registry);
        handler.handleRequest(request);

        context.assertIsSatisfied();
    }
}