package br.com.bhavantis.chatroom.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String = "",
    val topic: String = "",
    val sender: User,
    val receiver: User,
    val content: String
) {
    fun isSelf() = sender == receiver
}