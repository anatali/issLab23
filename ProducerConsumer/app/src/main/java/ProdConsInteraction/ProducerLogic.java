package ProdConsInteraction;

public class ProducerLogic  {
    private int distance = 0;

    public String getDistance( )  {
        distance += 10;
        return ""+distance;
    }


}
