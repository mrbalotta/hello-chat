package br.com.bhavantis.chatroom.channel.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bhavantis.chatroom.channel.domain.PrivateRoomRepository
import br.com.bhavantis.chatroom.core.domain.Chat
import br.com.bhavantis.chatroom.core.domain.ChatMessageObserver
import br.com.bhavantis.chatroom.core.domain.PrivateMessageRepository
import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.jinko.di.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class PrivateRoomViewModel: ViewModel() {

    private val privateRoomRepository: PrivateRoomRepository by inject()
    private val privateMessageRepository: PrivateMessageRepository by inject()
    private val chat: Chat by inject()
    private lateinit var speaker: User

    private val messagesLiveData = MutableLiveData<ChatMessage>()

    fun startPrivate(speaker: String) {
        viewModelScope.launch {
            val speakerUser = User(id = speaker)
            setSpeaker(speakerUser)
            if(chat.isConnected()) {
                subscribeToPrivateChannel()
                getAllPrivateMessages(speakerUser)
            }
        }
    }

    fun getMessageLiveData(): LiveData<ChatMessage> = messagesLiveData

    private suspend fun getAllPrivateMessages(user: User) {
        privateMessageRepository.getFrom(user.id)
    }

    private suspend fun subscribeToPrivateChannel() {
        val currentUser = privateRoomRepository.getCurrentUser()
        chat.subscribe("/user/${currentUser.id}/private", PrivateMessageObserver())
    }

    private fun setSpeaker(user: User) {
        speaker = user
    }

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            val currentUser = privateRoomRepository.getCurrentUser()
            val chatMessage = ChatMessage(
                sender = currentUser,
                receiver = speaker,
                content = msg
            )
            chat.send("/chat/private", chatMessage)
        }
    }

    private inner class PrivateMessageObserver: ChatMessageObserver {
        private val context = newSingleThreadContext("private-chat")
        override fun onMessageReceived(message: ChatMessage) {
            if(message.sender.id == speaker.id) {
                CoroutineScope(context).launch {
                    messagesLiveData.postValue(message)
                }
            }
        }
    }
}