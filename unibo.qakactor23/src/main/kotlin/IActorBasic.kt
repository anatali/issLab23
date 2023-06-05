package it.unibo.kactor

import kotlinx.coroutines.channels.SendChannel
import unibo.basicomm23.msg.ApplMessage

interface IActorBasic{
    fun getChannel() : SendChannel<ApplMessage>
}