package br.com.bhavantis.chatroom.channel.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.core.domain.Chat
import br.com.bhavantis.chatroom.core.domain.ChatMessageObserver
import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.jinko.di.inject
import kotlinx.coroutines.*

class ContactsViewModel: ViewModel() {
    private val contactsRepository: ContactsRepository by inject()
    private val chat: Chat by inject()
    private val contactLiveData = MutableLiveData<User>()
    private val allContactsLiveData = MutableLiveData<List<User>>()
    private val privateMessageLiveData = MutableLiveData<ChatMessage>()

    fun startUpdatingContactList() {
        viewModelScope.launch {
            if(chat.isConnected()) {
                chat.subscribe("/contacts", ContactsObserver())
                val user = contactsRepository.getCurrentUser()
                chat.subscribe("/user/${user.id}/private", PrivateMessageObserver())
            }
        }
    }

    fun getAllContacts() {
        viewModelScope.launch {
            val contacts = contactsRepository.getContacts()
            withContext(Dispatchers.Main) {
                allContactsLiveData.value = contacts
            }
        }
    }

    fun getAllContactsLiveData(): LiveData<List<User>> = allContactsLiveData
    fun getContactsLiveData(): LiveData<User> = contactLiveData
    fun getPrivateMessageLiveData(): LiveData<ChatMessage> = privateMessageLiveData

    private inner class ContactsObserver: ChatMessageObserver {
        override fun onMessageReceived(message: ChatMessage) {
            contactLiveData.postValue(message.sender)
        }
    }

    private inner class PrivateMessageObserver: ChatMessageObserver {
        private val context = newSingleThreadContext("contacts")

        override fun onMessageReceived(message: ChatMessage) {
            CoroutineScope(context).launch {
                privateMessageLiveData.postValue(message)
            }
        }

    }
}