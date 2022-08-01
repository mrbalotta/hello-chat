package br.com.bhavantis.chatroom.infrastructure.messaging

import kotlinx.coroutines.CoroutineDispatcher

interface MessagingBroker {
    fun connect(listener: ConnectionListener = DefaultConnectionListener())
    fun disconnect()
    fun send(destination: String, message: Any)
    fun subscribe(topic: String, listener: MessagingListener)
    fun getDispatcher(): CoroutineDispatcher
}