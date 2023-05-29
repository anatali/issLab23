package unibo.basicomm23.interfaces;

public interface Interaction2023 {
	public void forward(  IApplMessage msg ) throws Exception;
	public IApplMessage request(  IApplMessage msg ) throws Exception;
	public IApplMessage request(  IApplMessage msg, int tout ) throws Exception;
 	public void reply(  IApplMessage msg ) throws Exception;
 	public IApplMessage receive(  ) throws Exception ;
}
