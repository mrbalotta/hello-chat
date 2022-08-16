package br.com.bhavantis.chatroom.channel.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.core.domain.Chat
import br.com.bhavantis.chatroom.core.domain.ChatMessageObserver
import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.core.model.User
import br.com.bhavantis.chatroom.infrastructure.messaging.AbstractConnectionListener
import br.com.bhavantis.chatroom.infrastructure.messaging.ConnectionListener
import br.com.bhavantis.chatroom.infrastructure.messaging.MessagingBroker
import br.com.bhavantis.jinko.di.inject
import kotlinx.coroutines.*

class ContactsViewModel: ViewModel() {
    private val contactsRepository: ContactsRepository by inject()
    private val chat: Chat by inject()
    private val contactLiveData = MutableLiveData<ChatMessage>()
    private val privateMessageLiveData = MutableLiveData<ChatMessage>()
    private val allContactsLiveData = MutableLiveData<List<User>>()

    fun startUpdatingContactList() {
        viewModelScope.launch {
            Log.d("ALE", "start updating on ${Thread.currentThread().name}")
            chat.connect(AutoSubscription())
        }
    }

    fun getAllContacts() {
        viewModelScope.launch {
            Log.d("ALE", "get all contacts on ${Thread.currentThread().name}")
            val contacts = contactsRepository.getContacts().filter {
                it.id != contactsRepository.getCurrentUser().id
            }
            withContext(Dispatchers.Main) {
                Log.d("ALE", "received all contacts on ${Thread.currentThread().name}")
                if(contacts.isNotEmpty()) allContactsLiveData.value = contacts
            }
        }
    }

    fun getAllContactsLiveData(): LiveData<List<User>> = allContactsLiveData
    fun getContactLiveData(): LiveData<ChatMessage> = contactLiveData
    fun getPrivateMessageLiveData(): LiveData<ChatMessage> = privateMessageLiveData

    private inner class ContactsObserver: ChatMessageObserver {
        override fun onMessageReceived(message: ChatMessage) {
            Log.d("ALE", "contact received: ${message.sender} ")
            contactLiveData.postValue(message)
        }
    }

    private inner class PrivateMessageObserver: ChatMessageObserver {
        private val context = newSingleThreadContext("contacts")

        override fun onMessageReceived(message: ChatMessage) {
            CoroutineScope(context).launch {
                Log.d("ALE", "received private message on ${Thread.currentThread().name}")
                message.sender.sentMessage = true
                privateMessageLiveData.postValue(message)
            }
        }
    }

    private inner class AutoSubscription: AbstractConnectionListener() {
        override fun onOpened(broker: MessagingBroker) {
            viewModelScope.launch {
                Log.d("ALE", "auto subscription")
                chat.subscribe("/room/contacts", ContactsObserver())
                val user = contactsRepository.getCurrentUser()
                chat.subscribe("/user/${user.id}/private", PrivateMessageObserver())
            }
        }
    }
}