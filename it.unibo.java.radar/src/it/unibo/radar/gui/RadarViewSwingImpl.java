package it.unibo.radar.gui;
import eu.hansolo.steelseries.extras.Poi;
import eu.hansolo.steelseries.extras.Radar;
import eu.hansolo.steelseries.tools.BackgroundColor;
//import eu.hansolo.steelseries.tools.ForegroundType;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.FrameEffect;
import eu.hansolo.steelseries.tools.FrameType;
//import eu.hansolo.steelseries.tools.GaugeType;
//import eu.hansolo.steelseries.tools.LcdColor;
import it.unibo.radar.common.SonarRadarKb;
import it.unibo.radar.interfaces.IRadarViewSwingImpl;
//import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
//import java.awt.Graphics2D;
//import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.geom.Point2D;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class RadarViewSwingImpl extends GaugeViewSwingImpl implements IRadarViewSwingImpl {

  protected final Radar valueRadarField;
  private double mylat =  44.5151368;
  private double mylon =  11.3482484;
//  private Poi A_TARGET;
//  private Poi INTRUDER = null;
  private int myRange = SonarRadarKb.radarRange; //m
  private Poi MY_LOCATION = new Poi( "P0",  mylat, mylon ); //LAT, LON 44.5,11.34 89 ALT
//  private double deltaLon = 0.007; //0.035; at limit
//  private double deltaLat = 0.0025;
  protected Poi curPref = null;
//  private int nn = 0;
  private Panel displayPanel = new Panel();
  private Label displayLabel = new Label("STARTED");
  /*
   * 0.00001 	d = 0.7942856891525684
   * 0.0001		d = 7.9292288742391355
   * 0.001		d = 79.28921974457752
   * 0.01		d = 792.8923798224253
   * 0.015		d = 1189.3385706930774
   * 0.035		d = 2775.1233267929597
   * 0.02		d = 1585.7847627150563
   * 0.1		d = 7928.923356810496
   */
 
  
 /* { Constructors and factories */

  protected RadarViewSwingImpl(final String name  ) { 
	    super(name);
	    this.valueRadarField = new eu.hansolo.steelseries.extras.Radar() ;
	    setupValueField();
		//System.out.println("+++ getRotationAngle "  + valueRadarField.getRotationAngle());
  	    FlowLayout mainPanelLayout = new FlowLayout();
 	    mainPanelLayout.setAlignment(FlowLayout.CENTER);
	    this.getMainPanel().setLayout(mainPanelLayout);
	    displayLabel.setSize(100, 10);
	    displayLabel.setAlignment(Label.LEFT);
  	    displayPanel.add( displayLabel );
//	    BorderLayout mainPanelLayout = new BorderLayout();
//  	this.getMainPanel().add(displayPanel,BorderLayout.NORTH);
//	    this.getMainPanel().add(this.valueRadarField,BorderLayout.CENTER);	   
  	    this.getMainPanel().add( displayPanel );
	    this.getMainPanel().add(this.valueRadarField );	   
  	    updateDisplayMsg("IN ESPLORAZIONE");
   		valueRadarField.animate(true);
  }

  protected void updateDisplayMsg(String msg){
 	  displayLabel.setText(msg);
  }
  public static IRadarViewSwingImpl create(final String name ) {
    return new RadarViewSwingImpl(name  );
  }


  protected void setupValueField() {

       this.valueRadarField.setFrameDesign(FrameDesign.SHINY_METAL );
//      valueRadarField.setFrameDesign(FrameDesign.TILTED_GRAY);
//   		valueRadarField.setFrameType(FrameType.SQUARE); 
   		valueRadarField.setFrameType(FrameType.ROUND); 
   		this.valueRadarField.setFrameEffect(FrameEffect.EFFECT_BULGE);
   		this.valueRadarField.setBackgroundColor(BackgroundColor.STAINLESS);
   	    this.valueRadarField.setMinValue(0);
   	    this.valueRadarField.setMaxValue(500);
   	    this.valueRadarField.setMinMeasuredValueVisible(true);
   	    this.valueRadarField.setMaxMeasuredValueVisible(true);
   	    this.valueRadarField.setDigitalFont(true);
   	    this.valueRadarField.setTrackVisible(true);
//		   	valueRadarField.setForegroundVisible(true);
//		   	valueRadarField.setForeground(Color.green);
//		   	valueRadarField.setLcdColor(LcdColor.BLUE_LCD);
		   	 
 	  valueRadarField.setPreferredSize( new Dimension( 500, 0 ) );
 	  valueRadarField.setRange(myRange);	//3km
	  System.out.println("+++ getRange = " + valueRadarField.getRange() );
 	  valueRadarField.setMyLocation(MY_LOCATION );
	  System.out.println("+++ MY_LOCATION POI lat  =" + MY_LOCATION.getLat() + " lon=" + MY_LOCATION.getLon());
	  System.out.println("+++ MY_LOCATION POI locXY=" + MY_LOCATION.getLocationXY() );
   }
  
 @Override
 public void update(final String value) {
 		  EventQueue.invokeLater(new Runnable() {
	      @Override      
	      public void run() {   
//	    	  	nn++;
 	    	  	Struct tpos = (Struct) Term.createTerm(value);
	    	  	double distance = Double.parseDouble(""+tpos.getArg(1));
	    	  	double arg = Double.parseDouble(""+tpos.getArg(2));
 	    	  	updateDisplayMsg("P(" + distance + "," + arg+")");
 	    	  	//addNewTargetByMoving("PT"+nn, distance * 1000, arg); //VERSION WITH POINT NAME
 	    	  	addNewTargetByMoving("", distance * 1000, arg);
	       }
	    });
 
   }

 protected void addNewTargetByMoving( String name, double dist, double angle ){
	 	curPref = new Poi( name,  mylat, mylon ); 
  		Point2D ppos = curPref.shiftTo( dist, angle);
		valueRadarField.addPoi( curPref );	
  }


  @Override
  public ViewSwingFavor getFavor() {
    return ViewSwingFavor.HORIZONTAL;
  }

@Override
public String getShownValue() {	 
	return "";
}



}
