package br.com.bhavantis.chatroom.infrastructure.messaging

interface MessagingListener {
    fun onMessage(topic: String, message: String)
}