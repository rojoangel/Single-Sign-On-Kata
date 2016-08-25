package myservice;

import org.junit.Test;
import service.CredentialsRequest;
import sso.Response;

import static org.junit.Assert.assertEquals;

public class MyServiceTest {

    @Test
    public void salutes() throws Exception {
        MyService service = new MyService();
        Response response = service.handleRequest(new CredentialsRequest("Foo", null));
        assertEquals("hello Foo!", response.getText());
    }

}