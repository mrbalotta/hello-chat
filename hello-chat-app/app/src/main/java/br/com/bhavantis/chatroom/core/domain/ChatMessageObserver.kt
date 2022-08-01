package br.com.bhavantis.chatroom.core.domain

import br.com.bhavantis.chatroom.core.model.ChatMessage

interface ChatMessageObserver {
    fun onMessageReceived(message: ChatMessage)
}