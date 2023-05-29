package unibo.basicomm23.examples.prodcons;

public class Producer {
    private ProducerCaller prodCaller;

    public Producer(String name){

        ProducerLogic prodLogic = new ProducerLogic();
        prodCaller = new ProducerCaller(name, prodLogic,
                ProdConsConfig.protocol, ProdConsConfig.hostAddr, ProdConsConfig.entry);
    }

    public void start(){
        prodCaller.activate();
    }
}
