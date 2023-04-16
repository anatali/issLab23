package ProdConsInteraction;

public class Producer {
    private ProducerCaller prodCaller;

    public Producer(String name){

        ProducerLogic prodLogic = new ProducerLogic();
        prodCaller = new ProducerCaller(name, prodLogic,
                ProdConsInteraction.ProdConsConfig.protocol, ProdConsInteraction.ProdConsConfig.hostAddr, ProdConsInteraction.ProdConsConfig.entry);
    }

    public void start(){
        prodCaller.activate();
    }
}
