package unibo.appl1.http;

import org.junit.Before;
import org.junit.Test;
import unibo.appl1.daevitare.unibo.http.FlatApplication1HTTPNoStop;

public class TestFatApplication1HTTPNostop {
    protected FlatApplication1HTTPNoStop appl;
    @Before
    public void init() {
        initSystem();
    }
    protected void initSystem() {
        //Il robot deve essere in HOME !
        appl = new FlatApplication1HTTPNoStop();
    }
    @Test
    public void testWorkDone(){
        appl.walkAtBoundary();
        assert( appl.getNedges() == 4 ); //NON adeguato
    }
}
