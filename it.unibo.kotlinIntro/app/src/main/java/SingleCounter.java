package java;

public class SingleCounter {
    private static int counter = 0;
    public static int value(){ return counter;}
    public static void inc(){ counter++ ;}
    public static void dec(){ counter-- ;}
    public static void reset(){ counter = 0 ;}
}
