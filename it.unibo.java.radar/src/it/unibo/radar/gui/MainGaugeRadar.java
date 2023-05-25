package it.unibo.radar.gui;

import it.unibo.radar.interfaces.IPosition2D;
import it.unibo.radar.interfaces.IRadar;
import it.unibo.radar.interfaces.IRadarView;
import it.unibo.radar.interfaces.IRadarViewSwingImpl;
 
public class MainGaugeRadar {
protected IRadar   radar  ;
protected IRadarView  View ;
protected IRadarViewSwingImpl ViewImpl;
protected GaugeDisplaySwingImpl gaugeDisplayImpl;
protected CtrlDashSwingImpl ctrlDashImpl;

   protected void doJob(int w, int h) {
      try {
     	init( ) ;
      	start(w,h);
     } catch (Exception exc) {
       System.out.printf("Failed to start the subsystem. Reason: %s%n", exc.getMessage());
       exc.printStackTrace();
    }
   }

   protected void init(){
 	   initGauges( );
 	   initDashBoard( ) ;
    }
   
  
   protected void initGauges( ) {
 		 ViewImpl = RadarViewSwingImpl.create("RadarView" );	   
		 View  = RadarView.create( ViewImpl );			 	  
		 radar = RadarWithView.create( View);
   }
 
public void initDashBoard( ) {
    gaugeDisplayImpl =new GaugeDisplaySwingImpl(
		        "RadarDisplay", 
 		         ViewImpl
  		        );	   
	 ctrlDashImpl = CtrlDashSwingImpl.create("Dashboard", gaugeDisplayImpl  );
  } 
  protected void start(int w, int h){
	  ctrlDashImpl.start(w,h);	  
	  /*
	   * Update simulation
	   */
		 try {
			double distance = 64.00;
			double arg = 30;
//			for(int i = 1; i<=7; i++ ){
//				IPosition2D p0 = Position2D.createPolar(distance,arg);
//				radar.update( p0 );
//  				Thread.sleep(1000);
////				distance = distance - 5.0;
//				arg = arg + 20;
//			}
			IPosition2D p0 = Position2D.createPolar(35,20);
			radar.update( p0 );
			Thread.sleep(1000);
			p0 = Position2D.createPolar(53,90);
			radar.update( p0 );
			Thread.sleep(1000);
			p0 = Position2D.createPolar(70,110);
			radar.update( p0 );
		} catch (Exception e) {
	 		e.printStackTrace();
		}
  } 
  /*
   * MAIN
   */
  public static void main(String[] args) {
    System.out.println(">>> MainGauge Starting.");
    MainGaugeRadar system = new MainGaugeRadar();
    system.doJob(900,900);
  }
  


}
