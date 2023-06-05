package it.unibo.kactor

import it.unibo.kactor.MsgUtil.buildDispatch
import it.unibo.kactor.ActorBasic
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import unibo.basicomm23.utils.ColorsOut


class CoapObserverSupport(
    private val owner: ActorBasic,
    host: String?,
    port: String,
    private val ctx: String,
    private val actorName: String
) {
    private var relation: CoapObserveRelation? = null
    private var client: CoapClient? = null
    fun observe() {
        relation = client!!.observe( //
            object : CoapHandler {
                override fun onLoad(response: CoapResponse) {
                    val content = response.responseText
                    //ColorsOut.out("CoapObs | content=$content", ColorsOut.BLUE)
                    val actorDispatch = buildDispatch(
                        actorName, "coapUpdate",
                        "coapUpdate(RESOURCE, VALUE)".replace("RESOURCE", actorName).replace("VALUE", content),
                        owner.name
                    )
                    owner.sendMsgToMyself(actorDispatch)
                }

                override fun onError() {
                    ColorsOut.outerr("OBSERVING FAILED")
                }
            })
    }

    init {
        val addr = "coap://HOST:".replace("HOST", host!!) + port + "/CONTEXT/".replace("CONTEXT", ctx) + actorName
        //ColorsOut.out("CoapObs | addr=$addr", ColorsOut.BLUE)
        client = CoapClient(addr)
        observe()
    }
}