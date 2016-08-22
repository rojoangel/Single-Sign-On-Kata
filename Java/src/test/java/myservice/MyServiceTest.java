package myservice;

import org.junit.Test;
import sso.Request;
import sso.Response;

import static org.junit.Assert.*;

public class MyServiceTest {

    @Test
    public void salutes() throws Exception {
        MyService service = new MyService();
        Response response = service.handleRequest(new Request("Foo", null));
        assertEquals("hello Foo!", response.getText());
    }

}