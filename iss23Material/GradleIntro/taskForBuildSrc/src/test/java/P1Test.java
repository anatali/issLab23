/*
This class is compiled and executed by means of the custom tasks
defined in buildSrc/src/main/java/JavaCompileRun

It depends on the junit-4-13.jar stored in lib
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class P1Test {
    @Test
    public void testP1HasAGreeting() {
        P1 classUnderTest = new P1();
        String s = classUnderTest.getGreeting();
        System.out.println("P1Test | " + ( s != null) );
        assertNotNull("P1 should have a greeting", s);
        //assertNull("just to show an error ... ", s);
    }


    public static void main(String[] args){
        new P1Test().testP1HasAGreeting();
    }
}