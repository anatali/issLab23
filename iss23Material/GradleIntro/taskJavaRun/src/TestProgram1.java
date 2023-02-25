import org.junit.Test;
import static org.junit.Assert.*;

public class TestProgram1 {
    @Test public void testTheMain() {
        Program1 classUnderTest = new Program1();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
