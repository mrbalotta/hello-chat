package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.jinko.di.core.Component
import java.util.*

class ContactsRepoMock: ContactsRepository {
    override suspend fun getContacts(): List<User> {
        val list = mutableListOf<User>()
        list.let {
            it.add( User( UUID.randomUUID().toString(), "Ana", true ) )
            it.add( User( UUID.randomUUID().toString(), "Joao", true ) )
        }
        return list
    }

    override suspend fun getCurrentUser(): User {
        return UserRepoMock.user
    }
}