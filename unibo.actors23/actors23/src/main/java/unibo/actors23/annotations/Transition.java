package unibo.actors23.annotations;

import java.lang.annotation.*;

/*
 * Le annotazioni Java accettano solo attributi di tipo primitivo o Class
 */

@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Transitions.class)
public @interface Transition {
	String name() default "t0";
	String state()  ;
	String msgId() default "emptyMove" ;
	//Class guard() default GuardAlwaysTrue.class;
	String guard() default "";
	boolean interrupt() default false;
}
