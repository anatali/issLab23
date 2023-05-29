package unibo.basicomm23.interfaces;

import unibo.basicomm23.actors23.ActorBasic23;
import unibo.basicomm23.actors23.Proxy;
import unibo.basicomm23.msg.ProtocolType;

import java.util.Vector;

public interface IContext23 {
	public String getName();
	//public void activate();
	//public void deactivate();
	public Vector<String> getLocalActorNames( );
	public ActorBasic23 getActor(String actorName);
	public void addActor( ActorBasic23 a );
	public void removeActor(ActorBasic23 a);
	public  void showActorNames( );
	public void setActorAsRemote(String actorName,
		 String entry, String host, ProtocolType protocol );
	public Proxy getProxy(String actorName);
	public  void showProxies( );
}
