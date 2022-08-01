package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.channel.domain.PrivateRoomRepository
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.jinko.di.core.Component

class PrivateRoomRepoMock: PrivateRoomRepository {
    override suspend fun getCurrentUser(): User {
        return UserRepoMock.user
    }
}