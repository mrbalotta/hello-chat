package br.com.bhavantis.chatroom.infrastructure.messaging

interface ConnectionListener {
    fun onClosed(broker: MessagingBroker)
    fun onOpened(broker: MessagingBroker)
    fun onFailed(broker: MessagingBroker, throwable: Throwable)
    fun onHeartbeatFailed(broker: MessagingBroker, throwable: Throwable?)
}