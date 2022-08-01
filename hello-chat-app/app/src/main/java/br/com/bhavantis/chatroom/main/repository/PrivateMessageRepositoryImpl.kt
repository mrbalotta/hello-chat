package br.com.bhavantis.chatroom.main.repository

import br.com.bhavantis.chatroom.core.domain.PrivateMessageRepository
import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.jinko.di.core.Component
import br.com.bhavantis.jinko.di.core.Single

@Component
@Single
class PrivateMessageRepositoryImpl(

): PrivateMessageRepository {

    override suspend fun getFrom(id: String): List<ChatMessage> {
        return emptyList()
    }
}