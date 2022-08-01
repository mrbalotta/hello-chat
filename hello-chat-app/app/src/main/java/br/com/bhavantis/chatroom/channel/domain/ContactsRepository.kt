package br.com.bhavantis.chatroom.channel.domain

import br.com.bhavantis.chatroom.core.model.User

interface ContactsRepository {
    suspend fun getContacts(): List<User>
    suspend fun getCurrentUser(): User
}