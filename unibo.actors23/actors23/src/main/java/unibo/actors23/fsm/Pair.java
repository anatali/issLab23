package unibo.actors23.fsm;

public class Pair<A,B> {

    protected A first;
    protected B second;


    public Pair(A a, B b){
        first = a;
        second = b;
    }

    public A getFirst(){return first;};
    public B getSecond(){return second;};
}
