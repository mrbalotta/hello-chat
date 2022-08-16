package br.com.bhavantis.chatroom.core.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val nickname: String = "",
    var online: Boolean = false,
    var sentMessage: Boolean = false
)
