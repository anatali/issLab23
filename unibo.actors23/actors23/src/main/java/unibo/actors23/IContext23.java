package unibo.actors23;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ProtocolType;

import java.util.HashMap;
import java.util.Vector;

public interface IContext23 {
	public String getName();
	//public void activate();
	//public void deactivate();
	public Vector<String> getLocalActorNames( );
	public ActorBasic23 getActor(String actorName);
	//public HashMap<String, IApplMessage> getRequestMap();
	public void addActor( ActorBasic23 a );
	public void removeActor(ActorBasic23 a);
	public  void showActorNames( );
	public void setActorAsRemote(String actorName,
		 String entry, String host, ProtocolType protocol );
	public Proxy getProxy(String actorName);
	public void propagateEventToActors(IApplMessage event);
	public void propagateEventToProxies(IApplMessage event);
	public  void showProxies( );
}
