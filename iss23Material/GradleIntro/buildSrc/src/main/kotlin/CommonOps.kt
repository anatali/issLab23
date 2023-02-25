import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import javax.inject.Inject

// Inject annotation is required!
open class CommonTask @Inject constructor() : DefaultTask() {
    @Input var prefix ="buildSrc/CommonTask | "
    @Input var msg    ="Hello from CommonTask"
    @TaskAction
    fun commonOp() {
        println("$prefix $msg  ")
    }
}
