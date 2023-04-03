package unibo.appl1.observer;

import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Appl1ObserverForpath extends ApplAbstractObserver {
    private boolean applIsTerminated ;
    private Vector<String> moveHistory = new Vector<String>();
    private Set<String> moveCmds       = new HashSet<String>();

    public Appl1ObserverForpath(){
        moveCmds.add("robot-stepdone");
        moveCmds.add("robot-collision");
        moveCmds.add("robot-turnLeft");
        moveCmds.add("robot-athomeend");
    }

    public void init(){
        moveHistory = new Vector<String>();
    }

    @Override
    public void update(String msg) {
        if( moveCmds.contains(msg) )
            CommUtils.outgreen("   Appl1ObserverForpath:" + msg + " " + getCurrentPath());
        if( msg.contains("robot-stepdone")){ moveHistory.add("w");  }
        else if( msg.contains("robot-turnLeft")){
            moveHistory.add("l");
        }
        else if( msg.contains("robot-collision")){
            moveHistory.add("|");
            //String s1 = getPathAsCompactString();
            //if( s1.length() > 5) CommUtils.outred(s1);else
                //CommUtils.outgreen(s1);
        }
        else if( msg.equals("robot-athomeend") ){ setTerminated(); }
        //CommUtils.outgreen( getPath() );
    }

    private synchronized void setTerminated(){
        CommUtils.outmagenta("         Appl1ObserverForpath: Application TERMINATED");
        applIsTerminated =true;
        notifyAll(); //riattiva getPath
    }

    public String getCurrentPath(){
        return getPathAsCompactString();
    }

    public synchronized String getPath(){
        //CommUtils.outmagenta("Appl1ObserverForpath: getPath applIsTerminated=" + applIsTerminated);
        while( ! applIsTerminated ) {
            try {
                wait();
            } catch (InterruptedException e) {
                CommUtils.outred("Appl1ObserverForpath: getPath ERROR");
            }
        }
        //CommUtils.outmagenta("Appl1ObserverForpath: getPath RESUMED");
        return getPathAsCompactString();
    }

    private String getPathAsCompactString(){
        if( moveHistory.isEmpty()) return "nopath";
        String hflat = moveHistory.toString()
                .replace("[","")
                .replace("]","")
                .replace(",","")
                .replace(" ","");
        //CommUtils.outyellow("Appl1ObserverForpath: hflat=" + hflat);
        return hflat;
    }

    public boolean evalBoundaryDone(){
        String hflat      = getPath();  //bloccante
        String[] splitted = hflat.toString().split("l");
        //CommUtils.outyellow("Appl1ObserverForpath: splitted[0]=" + splitted[0]);
         boolean boundaryDone = splitted[0].length()==splitted[2].length()
                && splitted[1].length()==splitted[3].length();
        //the JVM disables assertion validation by default. Insert VM option -enableassertions
        //assert( boundaryDone );
        return boundaryDone;
    }
}


/*
 Non va con JUnit
    public  void waitTheUser(String msg) {
        try {

            Scanner sc = new Scanner(System.in);
            CommUtils.outblue(msg + " " + System.in + " " + sc);
            //String v = sc.nextLine();
            //CommUtils.outblue("1) "+v);
            int k = System.in.read();
            CommUtils.outblue("2) "+k);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
 */