package br.com.bhavantis.chatroom.core.domain

import br.com.bhavantis.chatroom.core.model.ChatMessage

interface PrivateMessageRepository {
    suspend fun getFrom(id: String): List<ChatMessage>
}