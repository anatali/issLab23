package unibo.appl1.http;
import org.junit.Before;
import org.junit.Test;
import unibo.appl1.observer.Appl1CoreTestStartStopObserver;
import unibo.basicomm23.utils.CommUtils;
import java.util.Vector;
import static org.junit.Assert.fail;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAppl1HTTPSprint2 {
    private Appl1CoreSprint2 appl;
    private Appl1CoreTestStartStopObserver obsStartStop ;

    @Before
    public void initSystemToTest(){
        try {
            appl         = new Appl1CoreSprint2();
            obsStartStop = new Appl1CoreTestStartStopObserver();
            appl.addObserver(obsStartStop);
            CommUtils.outmagenta("initSystem for testing done ");
        }catch( Exception e){
            fail("initSystemToTest " +e.getMessage());
        }
    }

    private void checkBoundaryDone(){
        boolean b = appl.evalBoundaryDone(); //bloccante
        CommUtils.outblue("checkBoundaryDone:" + b);
        assert( b );
    }

    public boolean evalBoundaryDone(){
        String hflat      = appl.getPath();  //bloccante
        String[] splitted = hflat.toString().split("l");
        boolean boundaryDone = splitted[0].length()==splitted[2].length()
                && splitted[1].length()==splitted[3].length();
        return boundaryDone;
    }

    protected void checkResumed(){
        obsStartStop.waitUntilResume();
    }

    protected void startTheApplication(){
             new Thread(){
                public void run() {
                    try {
                        appl.start();
                    } catch (Exception e) {
                        CommUtils.outred("startTheApplication ERROR: " + e.getMessage());
                        fail("startTheApplication " + e.getMessage());
                    }
                }
            }.start();
    }

    protected void checkHistoryAfterStop(){
        //CommUtils.delay(1500); //Per permettere elaborazione di ultime stringhe dall'observer
        Vector<String> h = obsStartStop.getMoveHistoryAfterStop();
        //h.forEach( item -> CommUtils.outyellow(item) );
        assert( h.elementAt(0).equals("robot-athomebegin"));
        assert( h.elementAt(1).equals("robot-moving"));
        if( h.size() > 3 ) assert( h.elementAt(2).equals("robot-stepdone"));
        //Dopo il secondo item ci possono essere altre coppie robot-moving / robot-stepdone
        assert( h.elementAt(h.size()-1).equals("robot-stopped"));
        CommUtils.outmagenta("checkHistoryAfterStop done") ;
    }



//Test coperto anche da testStop
    //@Test
    public void testStartNoStop(){
        CommUtils.outmagenta("testStartNoStop");
        startTheApplication();
        checkBoundaryDone();  //wait
    }

    @Test
    public void testStop(){
        CommUtils.outmagenta("testStop");
            startTheApplication();
            for( int i=1; i<=4; i++ ) {
                CommUtils.delay(5000);
                appl.stop();
                checkHistoryAfterStop();
                CommUtils.delay(1500);
                appl.resume();
                checkResumed();
            }
            //while( appl.isRunning){  CommUtils.delay(1000);  }
            evalBoundaryDone();
    }



}
