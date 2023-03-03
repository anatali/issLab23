import org.junit.Before;
import org.junit.Test;
import unibo.daevitare.unibo.http.FatApplication1HTTPNoStop;

public class TestFatApplication1HTTPNostop {
    protected FatApplication1HTTPNoStop appl;
    @Before
    public void init() {
        initSystem();
    }
    protected void initSystem() {
        //Il robot deve essere in HOME !
        appl = new FatApplication1HTTPNoStop();
    }
    @Test
    public void testWorkDone(){
        appl.walkAtBoundary();
        assert( appl.getNedges() == 4 ); //NON adeguato
    }
}
