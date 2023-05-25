package it.unibo.radar.common;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.radar.gui.CtrlDashSwingImpl;
import it.unibo.radar.gui.GaugeDisplaySwingImpl;
import it.unibo.radar.gui.Position2D;
import it.unibo.radar.gui.RadarView;
import it.unibo.radar.gui.RadarViewSwingImpl;
import it.unibo.radar.gui.RadarWithView;
import it.unibo.radar.interfaces.IPosition2D;
import it.unibo.radar.interfaces.IRadar;
import it.unibo.radar.interfaces.IRadarView;
import it.unibo.radar.interfaces.IRadarViewSwingImpl;
import it.unibo.system.SituatedPlainObject;
 

public class RadarControl extends SituatedPlainObject {
	protected IRadar radar;
	protected IRadarView View;
	protected IRadarViewSwingImpl ViewImpl;
	protected GaugeDisplaySwingImpl gaugeDisplayImpl;
	protected CtrlDashSwingImpl ctrlDashImpl;
 
 	
	public RadarControl(  IOutputView outView ) throws Exception {
		super( outView );
    	initGauges();
		initDashBoard();
	}
 	protected void initGauges() {
		ViewImpl = RadarViewSwingImpl.create("RadarView");
		View 	 = RadarView.create(ViewImpl);
		radar 	 = RadarWithView.create(View);
	}
	public void initDashBoard() {
		gaugeDisplayImpl = GaugeDisplaySwingImpl.create("RadarDisplay",
				ViewImpl);
		ctrlDashImpl = CtrlDashSwingImpl.create("Dashboard", gaugeDisplayImpl);
		ctrlDashImpl.start(SonarRadarKb.winWith,SonarRadarKb.winWith);
	}
	public void update( String dist, String theta ){		
		try {
			double distance = Double.parseDouble(dist);
			double arg 		= Double.parseDouble(theta);
			IPosition2D p0 	= Position2D.createPolar(distance,arg);
				 //outView.addOutput( "RadarControl UPDATE " + p0  );
				if( radar == null ) return;
				radar.update(p0);
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}
