package br.com.bhavantis.chatroom.registration.domain

import br.com.bhavantis.chatroom.core.model.User

interface UserRegistrationRepository {
    suspend fun save(user: User): User
    suspend fun setCurrentUser(user: User)
}