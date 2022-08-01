package br.com.bhavantis.chat.model

data class ChatUser(
    val id: String,
    val name: String,
    val online: Boolean = true
)