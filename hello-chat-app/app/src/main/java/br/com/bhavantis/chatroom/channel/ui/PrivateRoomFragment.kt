package br.com.bhavantis.chatroom.channel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.bhavantis.chatroom.R
import br.com.bhavantis.chatroom.channel.controller.PrivateRoomViewModel
import br.com.bhavantis.chatroom.core.model.ChatMessage

class PrivateRoomFragment: Fragment() {
    private val viewModel: PrivateRoomViewModel by viewModels()
    private val adapter = PrivateRoomAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMessageLiveData().observe(this) { onMessageArrived(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.private_room_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList(view)
        setupSendBtn(view)
        startPrivate()
    }

    private fun setupSendBtn(view: View) {
        val sendBtn = view.findViewById<Button>(R.id.sendBtn)
        val text = view.findViewById<EditText>(R.id.msgInput)
        sendBtn.setOnClickListener { sendMessage(text) }
    }

    private fun sendMessage(editText: EditText) {
        if(editText.text.isNotEmpty()) {
            viewModel.sendMessage(editText.text.toString())
            editText.text.clear()
        }
    }

    private fun setupList(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
    }

    private fun startPrivate() {
        arguments?.getString("speaker")?.let { viewModel.startPrivate(it) }
    }

    private fun onMessageArrived(chatMessage: ChatMessage) {
        adapter.addMessage(chatMessage)
    }
}