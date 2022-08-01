package br.com.bhavantis.chatroom.channel.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bhavantis.chatroom.R
import br.com.bhavantis.chatroom.channel.controller.ContactsViewModel
import br.com.bhavantis.chatroom.core.model.ChatMessage
import br.com.bhavantis.chatroom.core.model.User

class ContactsFragment: Fragment(), ContactSelectionListener {
    private val viewModel: ContactsViewModel by viewModels()
    private val adapter = ContactsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getContactsLiveData().observe(this) { onContactsArrived(it) }
        viewModel.getAllContactsLiveData().observe(this) { adapter.addAll(it) }
        viewModel.getPrivateMessageLiveData().observe(this) { onPrivateMessageArrived(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList(view)
        setupListUpdate()
    }

    private fun onPrivateMessageArrived(chatMessage: ChatMessage) {
        TODO("Not yet implemented")
    }

    private fun setupListUpdate() {
        viewModel.getAllContacts()
        viewModel.startUpdatingContactList()
    }

    private fun setupList(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.user_list)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
    }

    private fun onContactsArrived(user: User) {
        Log.d("ALE", "onContactsArrived ${user.nickname}")

        adapter.addContact(user)
    }

    private fun navigateToPrivate(speaker: User) {
        val bundle = Bundle().also { it.putString("speaker", speaker.id) }
        findNavController().navigate(R.id.contacts_to_private, bundle)
    }

    override fun onContactSelected(contact: User) {
        navigateToPrivate(contact)
    }
}