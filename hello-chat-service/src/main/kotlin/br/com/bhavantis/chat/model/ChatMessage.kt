package br.com.bhavantis.chat.model

data class ChatMessage(
    val id: String = "",
    val topic: String = "",
    val sender: User,
    val receiver: User,
    val content: String
) {
    fun isSelf() = sender == receiver
}