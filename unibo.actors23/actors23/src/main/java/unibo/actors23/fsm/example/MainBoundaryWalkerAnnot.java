/*
ClientUsingHttp.java
*/
package unibo.actors23.fsm.example;


import unibo.actors23.Actor23Utils;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

public class MainBoundaryWalkerAnnot {
	public void configureTheSystem(){
		String userDir = System.getProperty("user.dir");
		CommUtils.outblue("Working Directory = " + userDir);
		Actor23Utils.trace = true;
		Connection.trace   = true;
		Actor23Utils.createContexts("localhost",
				"C:/Didattica2023/issLab2023/unibo.actors23/actors23/src/main/java/unibo/actors23/fsm/example/boundaryWalkerAnnot.pl",
				"C:/Didattica2023/issLab2023/unibo.actors23/actors23/src/main/java/unibo/actors23/fsm/example/sysRules.pl");
	}
	public static void main(String[] args ){
		new MainBoundaryWalkerAnnot().configureTheSystem();
	}

//C:/Didattica2023/issLab2023/unibo.actors23/actors23/src/main/java/unibo/actors23/fsm/example/BoundaryWalkerAnnot.java
 }
