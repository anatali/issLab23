package unibo.actors23;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import org.junit.Before;
import org.junit.Test;
import unibo.basicomm23.utils.CommUtils;

import static org.junit.Assert.fail;

public class TestProlog {
    private String cmd;
    private Struct cmdAsTerm;

    @Before
    public void setUp() {
        cmd       = "msg( cmd, dispatch, main, led, turn(off), 1)";
        cmdAsTerm = (Struct) Term.createTerm(cmd);
        CommUtils.outblue("setUp " + cmdAsTerm);
    }
    @Test
    public void test0() {
        Struct payload   = (Struct) cmdAsTerm.getArg(4);
        assert( payload.toString().equals( "turn(off)" ));
        Term onOff       = payload.getArg(0);
        assert( onOff.toString().equals( "off" ));
    }

    @Test
    public void testUnify() {
        Prolog pengine = new Prolog();
        String cmdTemplate = "msg( cmd, MSGTYPE, main, led, turn(X), 1)";
        String goal      = cmdAsTerm+"="+cmdTemplate;
        try {
            SolveInfo sol = pengine.solve( goal+"." );
            Term msgType = sol.getVarValue("MSGTYPE");
            assert( msgType.toString().equals( "dispatch" ));
            Term data = sol.getVarValue("X");
            assert( data.toString().equals( "off") );
        } catch ( Exception e) {
            fail("testUnify " + e.getMessage());
        }
    }
}
