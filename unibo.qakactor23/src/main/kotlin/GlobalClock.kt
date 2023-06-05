package it.unibo.kactor

object GlobalClock{
    private var timeAtStart: Long = 0

    fun startTimer() {
        timeAtStart = System.currentTimeMillis()
    }

    fun stopTimer() : Int{
        val duration = (System.currentTimeMillis() - timeAtStart).toInt()
        //println("DURATION = $duration")
        return duration
    }
}