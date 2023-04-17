package ProdConsActors23;

public class ProducerLogic {
    private static int distance = 0;

    public  String getDistance( )  {
        //synchronized(this){ProducerLogic.distance = ProducerLogic.distance + 10;}
        distance = distance + 10;
        return ""+ProducerLogic.distance;
    }


}
