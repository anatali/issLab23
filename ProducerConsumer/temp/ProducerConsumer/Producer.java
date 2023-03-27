package ProducerConsumer;

public class Producer {
    private ProducerCaller prodCaller;

    public Producer(String name){

        ProducerLogic prodLogic = new ProducerLogic();
        prodCaller = new ProducerCaller(name, prodLogic,
                ProducerConsumer.ProdConsConfig.protocol, ProducerConsumer.ProdConsConfig.hostAddr, ProducerConsumer.ProdConsConfig.entry);
    }

    public void start(){
        prodCaller.activate();
    }
}
