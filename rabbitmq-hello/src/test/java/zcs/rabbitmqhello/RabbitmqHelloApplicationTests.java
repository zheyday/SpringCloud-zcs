package zcs.rabbitmqhello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqHelloApplicationTests {

    @Autowired
    private Sender sender;

    @Test
    public void contextLoads() throws Exception{
        sender.send();
    }

    @Test
    public void send1() throws Exception {
        sender.send1();
    }

    @Test
    public void send2() throws Exception {
        sender.send2();
    }

    @Test
    public void send3() throws Exception {
        sender.send3();
    }

    @Test
    public void sendTTL() throws Exception {
        sender.sendTTL();
    }
}
