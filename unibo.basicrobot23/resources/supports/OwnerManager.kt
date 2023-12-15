package supports

import unibo.basicomm23.utils.CommUtils

object OwnerManager {
    var owner = "unknown"
    val consoleName = "gui23xyz9526" 
    var steptime    = "330"

    fun checkOwner(caller: String): Boolean {
        //CommUtils.outblue("OwnerManager | checkOwner $caller")
        if( caller == "planexec" ||
            caller == "robotpos" ||
            caller ==  consoleName
            ) return true
        else return owner == caller
    }
    fun engage(caller: String) : Boolean{
        if( owner == "unknown" || owner == caller || caller == consoleName){
            if(caller != consoleName) owner = caller
            return true
        }
        else return false
    }
    fun disengage(){
        owner = "unknown"
    }
    fun setStepTime(t: String ){
        steptime=t
    }
}