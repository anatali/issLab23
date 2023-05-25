package it.unibo.radar.gui;
 

import java.math.BigDecimal;

import it.unibo.radar.interfaces.IGaugeDisplaySwingImpl; 

public class CtrlDashSwingImpl extends DashSwing  {

  /* { Constructors and factories */

 protected CtrlDashSwingImpl( final String name ) {
	 super(name);
 }
  protected CtrlDashSwingImpl(final String name,
      final IGaugeDisplaySwingImpl gaugeDisplay ) {

    super(name);
    this.addComponent(gaugeDisplay.getMainPanel(), 0, 0, 1, 1, 4.0, 4.0);
//    if( cmdDisplay!=null ) this.addComponent(cmdDisplay.getMainPanel(), 1, 0, 1, 1, 1.0, 1.0);
    this.frame.setSize(400,400); //by AN
  }

  public static CtrlDashSwingImpl create(final String name ) {
	 //System.out.println("%%% CtrlDashSwingImpl"  );
    return new CtrlDashSwingImpl( name );
  }
  public static CtrlDashSwingImpl create(final String name,
	      final IGaugeDisplaySwingImpl gaugeDisplay ) {
		  //System.out.println("%%% CtrlDashSwingImpl"  );
	    return new CtrlDashSwingImpl(name, gaugeDisplay );
	  }

 
   @Override
  public void start(int w, int h) {
    super.start(w,h);
  }

   @Override
  public void stop() {
    super.stop();
  }

  
  /* { Usage example */

//  public static void main(String[] args) {
//    GaugeValueFactory gaugeValueFactory = new GaugeValueFactory();
//    CmdDisplaySwingImpl cmdDisplay = CmdDisplaySwingImpl.create("CmdDisplay");
//    TachometerViewSwingImpl tachometerView = TachometerViewSwingImpl.create(
//        "Tachometer", "x1",
//        gaugeValueFactory.createRevol(BigDecimal.valueOf(2.0)),
//        gaugeValueFactory.createRevol(BigDecimal.valueOf(120.0)));
//  
//    GaugeDisplaySwingImpl gaugeDisplay = GaugeDisplaySwingImpl.create(
//            "Gauge Display", tachometerView );
//    CtrlDashSwingImpl ctrlDash = CtrlDashSwingImpl.create(
//        "Control Dashboard");
//    
//    	ctrlDash.addComponent(gaugeDisplay.getMainPanel(), 0, 0, 1, 1, 4.0, 4.0);
//    	ctrlDash.addComponent(cmdDisplay.getMainPanel(), 1, 0, 1, 1, 1.0, 1.0);
//        ctrlDash.start(SysKb.windowWidth, SysKb.windowHeight);
//     }
 

}
