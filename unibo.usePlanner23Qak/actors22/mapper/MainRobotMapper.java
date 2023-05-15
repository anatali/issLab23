package mapper;


@Context22(name = "pcCtx", host = "localhost", port = "8075")
//@Actor22(name = MainRobotMapper.robotName, contextName = "pcCtx", implement = RobotMapperBoundary.class)
//@Actor22(name = MainRobotMapper.robotName, contextName = "pcCtx", implement = RobotMapperPlans.class)
@Actor22(name = MainRobotMapper.robotName, contextName = "pcCtx", implement = RobotUsingMap.class)
public class MainRobotMapper {
	
	public static final String robotName = "mapper";
	
	public void doJob() {
		CommSystemConfig.tracing = false;
		Qak22Context.configureTheSystem(this);
		Qak22Context.showActorNames();
  		Qak22Util.sendAMsg( SystemData.startSysCmd("main",robotName) );
	};

	public void terminate() {
		CommUtils.aboutThreads("Before end - ");		
		CommUtils.delay(60000); //Give time to work ...
		CommUtils.aboutThreads("At exit - ");		
		System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		MainRobotMapper appl = new MainRobotMapper( );
		appl.doJob();
		appl.terminate();
	}

}
