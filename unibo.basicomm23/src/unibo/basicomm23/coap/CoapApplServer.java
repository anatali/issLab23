package unibo.basicomm23.coap;

import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import org.eclipse.californium.core.server.resources.Resource;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import unibo.basicomm23.utils.Connection;

public class CoapApplServer extends CoapServer { //implements IContext

	private static CoapResource root      = new CoapResource("basicomm23"); //new CoapResourceCtx("actors", null);
	private static CoapApplServer server  = null;

	public CoapApplServer( int port ) {
		super(port);
		add( root );
		if( Connection.trace )  CommUtils.outyellow("    +++ CoapApplServer | STARTED root=" + root + " port="+port );
		server = this;
		start();
	}
	
	public static CoapApplServer getTheServer() {
 		return server;
	}
	public static void stopTheServer() {
		if( server != null ) {
			server.stop();
			server.destroy();
			ColorsOut.out("    +++ CoapApplServer | STOPPED", ColorsOut.GREEN );
		}
	}

	public static Resource getResource( String uri ) {
		if( Connection.trace ) CommUtils.outblue("    +++ CoapApplServer | getResource uri="  + uri + " root=" + root.getName());
		return getResource( root, uri );
	}
	
	//Depth-first research
	private static Resource getResource(Resource root, String uri) {
		if( root != null ) {
			if( Connection.trace ) CommUtils.outyellow("    +++ CoapApplServer | getResource checks in: " + root.getName() + " for uri=" + uri);
			Collection<Resource> rootChilds = root.getChildren();
			Iterator<Resource> iter         = rootChilds.iterator();
			if( Connection.trace )  ColorsOut.out("    +++ CoapApplServer | getResource child size:"+rootChilds.size());
			while( iter.hasNext() ) {
				Resource curRes = iter.next();
				String curUri   = curRes.getURI();
				if( Connection.trace ) CommUtils.outyellow("    +++ CoapApplServer | getResource curUri:"+curUri);
				if( curUri.equals(uri) ){
					if( Connection.trace ) CommUtils.outyellow("    +++ CoapApplServer | getResource finds "+ curRes.getName() + " for " + curUri);
					return  curRes;
				}else { 
					//if( Connection.trace ) CommUtils.outyellow("    +++ CoapApplServer | getResource restart from:"+curRes.getName());
					Resource subRes = getResource(curRes,uri); 
					if( subRes != null ) return subRes;					
				}
			}//while  (all sons explored)
		}
		return null;
	}
 
	public  void addCoapResource( CoapResource resource, String fatherUri  )   {
		if( Connection.trace ) CommUtils.outblue("    +++ CoapApplServer | addCoapResource resource=" + resource.getName() + " fatherUri=" + fatherUri );
		Resource res = getResource("/"+fatherUri);
		root.add(resource);
		  res = getResource( "/basicomm23/"+resource.getName());
		//if( Connection.trace ) CommUtils.outred("    +++ CoapApplServer | addCoapResource res= " + res);
	}

}
