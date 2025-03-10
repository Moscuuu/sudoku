import org.example.Greeter;
import org.example.MessageFormatter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TestGreeter {
    public TestGreeter(){
    }
    @Test
    public void GreeterTest(){
        MessageFormatter formatter = new MessageFormatter();
        Greeter greeter = new Greeter(formatter);
        String name = "Student";
        String expResult = "Hello Student";
        String result = greeter.greet(name);
        assertEquals(expResult,result);
    }
    @Test
    public void NullTest(){
        MessageFormatter formatter = new MessageFormatter();
        Greeter greeter = new Greeter(formatter);
        String name = null;
        String expResult = "Hello null";
        String result = greeter.greet(name);
        assertEquals(expResult,result);
    }
    @Test
    public void EmptyTest(){
        MessageFormatter formatter = new MessageFormatter();
        Greeter greeter = new Greeter(formatter);
        String name ="";
        String expResult = "Hello ";
        String result = greeter.greet(name);
        assertEquals(expResult,result);
    }
}
