package br.com.bhavantis.chatroom.channel.ui

import br.com.bhavantis.chatroom.core.model.User

interface ContactSelectionListener {
    fun onContactSelected(contact: User)
}