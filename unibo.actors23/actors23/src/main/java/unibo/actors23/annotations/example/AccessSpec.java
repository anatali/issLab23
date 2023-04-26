/**
 ProtocolSpec.java
 ===============================================================
 User-defined annotation related to communication protocols
 ===============================================================
 */
package unibo.actors23.annotations.example;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE   })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AccessSpec {
    public enum issProtocol {UDP,TCP,HTTP,MQTT,COAP,WS} ;
    issProtocol protocol() default issProtocol.TCP;
    String url() default "unknown";
    String configFile() default "ProtocolConfig.txt";
}
