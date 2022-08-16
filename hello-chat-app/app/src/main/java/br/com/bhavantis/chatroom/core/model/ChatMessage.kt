package br.com.bhavantis.chatroom.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    var id: String = "SELF",
    val topic: String = "",
    val sender: User,
    val receiver: User,
    val content: String
) {
    fun isSelf() = id == "SELF"
}