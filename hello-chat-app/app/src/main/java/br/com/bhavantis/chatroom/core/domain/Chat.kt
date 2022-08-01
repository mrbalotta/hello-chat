package br.com.bhavantis.chatroom.core.domain

import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.infrastructure.messaging.ConnectionListener
import br.com.bhavantis.chatroom.infrastructure.messaging.MessagingListener

interface Chat {
    fun connect(listener: ConnectionListener)
    fun send(destination: String, message: ChatMessage)
    fun subscribe(topic: String, listener: ChatMessageObserver)
    fun isConnected(): Boolean
}