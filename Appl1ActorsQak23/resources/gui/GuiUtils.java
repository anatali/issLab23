package gui;

import it.unibo.kactor.ActorBasic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GuiUtils {

	public static void showSystemInfo(){

		System.out.println(
			"Utils  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		System.out.println(
			"Utils AT START | num of threads="+ Thread.activeCount() +" currentThread=" + Thread.currentThread() );
	}

	public static Frame initFrame(int dx, int dy){
 		Frame frame        = new Frame();
 		ImageIcon img      = new ImageIcon("./resources/consolegui/mbot-S.jpg");
 		frame.setIconImage(img.getImage());
 		BorderLayout layout = new BorderLayout();
 		frame.setSize( new Dimension(dx,dy) );
 		frame.setLayout(layout);		
 		frame.addWindowListener(new WindowListener() {			
			@Override
			public void windowOpened(WindowEvent e) {}				
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);				
			}			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		}); 	
		frame.setVisible(true);
		return frame;
		
	}
 	public static Frame initFrame(){
 		return initFrame(400,200);
 	}

	public static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void createGui(ActorBasic owner) {
		String[] buttonLabels  = new String[] {"start", "stop", "resume", "getpath" };
        ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
        concreteButton.addObserver( new GuiObsForQak(owner) );

	}
}
