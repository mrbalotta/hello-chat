package br.com.bhavantis.chatroom.channel.domain

import br.com.bhavantis.chatroom.core.model.User

interface PrivateRoomRepository {
    suspend fun getCurrentUser(): User
}