package unibo.basicomm23.examples.prodcons;

public class ProducerLogic  {
    private int distance = 0;

    public String getDistance( )  {
        distance += 10;
        return ""+distance;
    }


}
