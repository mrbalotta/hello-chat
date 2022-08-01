package br.com.bhavantis.chatroom.core.domain

import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.infrastructure.logging.DroidLogger
import br.com.bhavantis.chatroom.infrastructure.messaging.ConnectionListener
import br.com.bhavantis.chatroom.infrastructure.messaging.MessagingBroker
import br.com.bhavantis.chatroom.infrastructure.messaging.MessagingListener
import br.com.bhavantis.chatroom.infrastructure.parsing.JsonParser
import br.com.bhavantis.jinko.di.core.Component
import br.com.bhavantis.jinko.di.core.ComponentMapping
import br.com.bhavantis.jinko.di.core.Single
import kotlinx.coroutines.DelicateCoroutinesApi

@Single
@Component
@ComponentMapping(Chat::class)
@OptIn(DelicateCoroutinesApi::class)
class ChatImpl(
    private val broker: MessagingBroker,
    private val logger: DroidLogger,
    private val parser: JsonParser
): Chat, MessagingListener {

    private val observers = mutableMapOf<String, MutableList<ChatMessageObserver>>()
    private var connected = false

    override fun connect(listener: ConnectionListener) {
        broker.connect(ConnectionListenerWrapper(listener))
    }

    override fun send(destination: String, message: ChatMessage) {
        broker.send(destination, message)
    }

    override fun subscribe(topic: String, listener: ChatMessageObserver) {
        if(!observers.containsKey(topic)) {
            observers[topic] = mutableListOf()
        }
        observers[topic]!!.add(listener)
        broker.subscribe(topic, this)
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun onMessage(topic: String, message: String) {
        logger.info("ICHAT", "running 'StompMessagingBroker.onMessage (before coroutine)' on: ${Thread.currentThread().name}")
        val parsedMessage = parser.fromJson(message, ChatMessage::class.java)
        observers[topic]?.forEach { it.onMessageReceived(parsedMessage) }
    }

    private inner class ConnectionListenerWrapper(
        private val connectionListener: ConnectionListener
    ): ConnectionListener by connectionListener {

        override fun onOpened(broker: MessagingBroker) {
            connected = true
            connectionListener.onOpened(broker)
        }

        override fun onClosed(broker: MessagingBroker) {
            connected = false
            connectionListener.onClosed(broker)
        }

        override fun onFailed(broker: MessagingBroker, throwable: Throwable) {
            observers.clear()
            connectionListener.onFailed(broker, throwable)
        }
    }
}