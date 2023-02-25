/*
This class is compiled and executed by means of the custom tasks
defined in buildSrc/src/main/java/JavaCompileRun
 */

public class P1 {
    public static String getGreeting() {
        return "Hello from P1 written in Java";
    }

    public static void main(String... args) {
        System.out.println( getGreeting() );
    }
}