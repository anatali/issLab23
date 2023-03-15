package unibo.appl1.common;

public class VrobotMsgs {
    public final static String turnrightcmd =
            "{\"robotmove\":\"turnRight\"    , \"time\": \"300\"}";
    public final static String turnleftcmd  =
            "{\"robotmove\":\"turnLeft\"     , \"time\": \"400\"}";
    public final static String haltcmd      =
            "{\"robotmove\":\"alarm\" ,        \"time\": \"10\"}";
    public final static String stepcmd      =
            "{\"robotmove\":\"moveForward\"  , \"time\": \"350\"}";

    public final static String forwardcmd   =
            "{\"robotmove\":\"moveForward\"  , \"time\": TIME}";
    public final static String backwardcmd  =
            "{\"robotmove\":\"moveBackward\" , \"time\": TIME}";

    public final static String forwardlongcmd   =
            "{\"robotmove\":\"moveForward\"  , \"time\": \"2300\"}";
}
