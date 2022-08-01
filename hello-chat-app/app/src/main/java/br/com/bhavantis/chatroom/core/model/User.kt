package br.com.bhavantis.chatroom.core.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val nickname: String = "",
    val online: Boolean = false,
    val sentMessage: Boolean = false
)
