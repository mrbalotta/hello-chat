package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.channel.domain.PrivateRoomRepository
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.chatroom.registration.domain.UserRegistrationRepository
import br.com.bhavantis.jinko.di.inject
import io.ktor.client.*
import io.ktor.client.request.*

object UserRepositoryImpl: UserRegistrationRepository, ContactsRepository, PrivateRoomRepository {

    private val config: ApiConfig by inject()
    private val httpClient: HttpClient by inject()

    private lateinit var currentUser: User

    override suspend fun getContacts(): List<User> {
        return httpClient.get("${config.getUrl()}/contacts")
    }

    override suspend fun getCurrentUser(): User {
        return currentUser
    }

    override suspend fun save(user: User): User {

        return httpClient.post("${config.getUrl()}/contacts") {
            body = user
        }
    }

    override suspend fun setCurrentUser(user: User) {
        currentUser = user
    }
}