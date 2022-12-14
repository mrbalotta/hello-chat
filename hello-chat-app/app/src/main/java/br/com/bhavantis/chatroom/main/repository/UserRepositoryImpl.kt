package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.channel.domain.PrivateRoomRepository
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.chatroom.registration.domain.UserRegistrationRepository
import br.com.bhavantis.jinko.di.inject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object UserRepositoryImpl: UserRegistrationRepository, ContactsRepository, PrivateRoomRepository {

    private val config: ApiConfig by inject()
    private val httpClient: HttpClient by inject()

    private lateinit var currentUser: User

    override suspend fun getContacts(): List<User> {
        return httpClient.get("${config.getUrl()}/contacts").body()
    }

    override suspend fun getCurrentUser(): User {
        return currentUser
    }

    override suspend fun save(user: User): User {
        return httpClient.post("${config.getUrl()}/contacts") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
    }

    override suspend fun setCurrentUser(user: User) {
        currentUser = user
    }
}