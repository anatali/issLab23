package unibo.actors23.annotations.example;

import unibo.basicomm23.utils.CommUtils;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@AccessSpec(
        protocol   = AccessSpec.issProtocol.HTTP,
        url        = "http://localHost:8090/api/move"
        //, configFile ="myfile.txt"
)
public class AnnotationUsageDemo {
	
    //  line example: http://localHost:8090/api/move
    public String getHostAddr(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        CommUtils.outgreen("getHostAddr | line: " + line);
        String content = null;
        if( matcher.find()) {
//            int end   = matcher.end() ;
//            int start = matcher.start();
//            CommUtils.outblue("start: " + start);
//            CommUtils.outblue("end:   " + end,   ColorsOut.CYAN);
//            CommUtils.outblue("group: " + matcher.groupCount(),   ColorsOut.CYAN);
            for( int i = 1; i<=5; i++ ) {
                CommUtils.outgreen("getHostAddr | group " + i + ":" + matcher.group(i));
            }
            content = matcher.group(2)+":"+matcher.group(3);
//            String a = line.substring(start,end); //uguale a  matcher.group(0)
//            CommUtils.outblue("a:                     " + a,   ColorsOut.CYAN);
         }
        return content;
    }
    
    
    public void readProtocolAnnotation(Object element) {
        try {
            Class<?> clazz            = element.getClass();
            Annotation[] annotations  = clazz.getAnnotations();
             for (Annotation annot : annotations) {
                 if (annot instanceof AccessSpec) {
                	AccessSpec p  = (AccessSpec) annot;
                    CommUtils.outblue("Tipo del protocollo: " + p.protocol());
                    CommUtils.outblue("Url del protocollo:  " + p.url());
                     CommUtils.outblue("Configuration file: " + p.configFile());
                    //esempio: http://localHost:8090/api/move
                    String v = getHostAddr("(\\w*)://([a-zA-Z]*):(\\d*)/(\\w*)/(\\w*)", p.url());
                    CommUtils.outblue("hostAddr= " + v);
               }
            }
        } catch (Exception e) {
            CommUtils.outred("readAnnotation |  ERROR:" + e.getMessage());
        }
    }
	
	public AnnotationUsageDemo() {
		readProtocolAnnotation( this );			
	}
 	
	public static void main( String[] args) {
		new AnnotationUsageDemo();
	}
}
