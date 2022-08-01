package br.com.bhavantis.chat.model

data class User(
    val id: String = "",
    val nickname: String = "",
    val online: Boolean = false,
    val sentMessage: Boolean = false
)
