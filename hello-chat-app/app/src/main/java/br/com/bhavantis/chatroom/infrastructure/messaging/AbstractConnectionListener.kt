package br.com.bhavantis.chatroom.infrastructure.messaging

abstract class AbstractConnectionListener: ConnectionListener {
    override fun onClosed(broker: MessagingBroker) {}
    override fun onOpened(broker: MessagingBroker) {}
    override fun onFailed(broker: MessagingBroker, throwable: Throwable) {}
    override fun onHeartbeatFailed(broker: MessagingBroker, throwable: Throwable?) {}
}