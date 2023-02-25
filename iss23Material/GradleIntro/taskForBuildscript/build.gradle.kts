import org.gradle.internal.impldep.org.mozilla.javascript.tools.shell.Global.readline

println(" ...... build in taskForBuildscript sub-project  ")

/*
plugins{
    id("unibo.disi.builder") version "1.0"
}

*/


/*
In this subproject, ...
 */

buildscript {
    repositories {
        mavenCentral()
        //jcenter()
        //maven { url = uri("file://C:\\Didattica2018Work\\iss2021Lab\\uniboRepos\\maven-repo") }  //OK
        maven ( url= "../../uniboRepos/maven-repo" )
        flatDir {   dirs("../../unibolibs")	 }
     }
    // everything listed in the dependencies is actually a plugin,
    // which we'll do "apply plugin" in our module level gradle file.
    //https://medium.com/@StefMa/its-time-to-ditch-the-buildscript-block-a1ab12e0d9ce
    dependencies {
        // ~/.m2/repository/  com/company/product/plugin/ product-gradle-plugin/1.0/product-gradle-plugin-1.0.jar
        //classpath 'com.company.product.plugin:        product-gradle-plugin:1.0'
        //classpath( "unibo.disi.plugin:unibo.disi.builder.gradle.plugin:1.0" )
        classpath( "unibo.disi.builder:unibo.disi.builder.gradle.plugin:1.0" )
    }

    //apply(plugin = "it.unibo.disi.mydisiBuilderKotlinPlugin")  //not found

}


//apply { plugin("unibo.disi.builder")  }       //OK
apply(plugin = "unibo.disi.builder")            //OK
/*
1. You can code the plugin directly within your Gradle build script.
2. You can put the plugin under buildSrc (ex. buildSrc/src/main/...).
3. You can import your custom plugin as a jar in your buildscript method.
*/

//gradle -q :taskForBuildscript:tasks --all INCLUDES buildDisi and hellobs

//extend the task buildDisi defined by the plugin unibo.disi.builder
tasks.named("buildDisi"){ //Accessing a task via API - adding behaviour
    doFirst {
        println("STARTING the DISI-BUILDING ...  ${this.name}")
    }
}


task("jobbs") {
    doLast {	//a shortcut to define an action
        val v = unibo.disi.builder.genUtils.genFilePathName("xxx")
        println("${this.name} has used the 'unibo.disi.builder' plugin to generate the path: $v")
    }
}
