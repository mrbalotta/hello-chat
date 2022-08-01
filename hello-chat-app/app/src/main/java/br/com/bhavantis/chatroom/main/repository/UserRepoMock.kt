package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.chatroom.registration.domain.UserRegistrationRepository
import br.com.bhavantis.jinko.di.core.Component
import java.util.*

class UserRepoMock: UserRegistrationRepository {

    companion object {
        val user = User(
            id = UUID.randomUUID().toString(),
            nickname = "Ale Nick"
        )
    }

    override suspend fun save(user: User): User {
        return user
    }

    override suspend fun setCurrentUser(user: User) {

    }
}