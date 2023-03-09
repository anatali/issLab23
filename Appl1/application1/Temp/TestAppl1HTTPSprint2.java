package unibo.appl1.http;
import org.junit.Before;
import org.junit.Test;
import unibo.appl1.common.Appl1CoreTestingObserver;
import unibo.basicomm23.utils.CommUtils;
import java.util.Vector;
import static org.junit.Assert.fail;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAppl1HTTPSprint2 {
    protected Appl1Core appl;
    protected Appl1CoreTestingObserver obs ;


    @Before
    public void initSystemToTest(){
        appl = new Appl1Core(); //new Appl1HTTPSprint2();
        obs  = new Appl1CoreTestingObserver();
        obs.init();
        appl.addObserver( obs );
        CommUtils.outmagenta("initSystem done "  );
    }

    protected boolean checkTerminated(){
        obs.waitApplTerminated();
        return true;
    }

    protected void startTheApplication(){
        new Thread(){
            public void run(){
                try {
                    appl.start();
                } catch (Exception e) {
                    CommUtils.outred("startTheApplication ERROR" + e.getMessage());
                    fail("startTheApplication " + e.getMessage());
                }
            }
        }.start();
    }

    protected void checkHistoryAfterStop(){
        CommUtils.delay(1500); //Per permettere elaborazione di ultime stringhe
        Vector<String> h = obs.getMoveHistory();
        //h.forEach( item -> CommUtils.outyellow(item) );
        assert( h.elementAt(0).equals("robot-athomebegin"));
        assert( h.elementAt(1).equals("robot-moving"));
        if( h.size() > 3 ) assert( h.elementAt(2).equals("robot-stable"));
        //Dopo il secondo item ci possono essere altre coppie robot-moving / robot-stable
        assert( h.elementAt(h.size()-1).equals("robot-stopped"));
    }


    @Test
    public void testStartNoStop(){
        CommUtils.outmagenta("testStartNoStop");
        startTheApplication();
        assert( checkTerminated() );
    }

    @Test
    public void testStop(){
        try {
            startTheApplication();
            CommUtils.delay(3000); //Attendo un pò ...
            appl.stop();
            checkHistoryAfterStop();
            CommUtils.delay(1000 );//Attendo un pò ...
            continueToHome();
        } catch (Exception e) {
            fail("TestAppl1HTTPLocal | testStart " + e.getMessage());
        }
    }

    protected void continueToHome(){
        appl.resume();
        assert( checkTerminated() );
    }

    //@Test
    public void testBoundary(){
        CommUtils.outmagenta("testBoundary");
        startTheApplication();
        obs.waitApplTerminated();
        assert( obs.evalBoundaryDone() );
    }
}
