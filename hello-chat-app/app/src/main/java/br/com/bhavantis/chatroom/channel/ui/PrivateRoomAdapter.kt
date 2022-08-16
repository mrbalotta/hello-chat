package br.com.bhavantis.chatroom.channel.ui

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.bhavantis.chatroom.R
import br.com.bhavantis.chatroom.core.model.ChatMessage

internal class PrivateRoomAdapter(dataset: MutableList<ChatMessage> = mutableListOf()):
    AbstractChatAdapter<PrivateRoomAdapter.ViewHolder>(dataset) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val username: TextView
        val date: TextView
        val message: TextView

        init {
            username = view.findViewById(R.id.username)
            date = view.findViewById(R.id.message_date)
            message = view.findViewById(R.id.message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(getView(parent, viewType))
    }

    override fun getLayoutLeft(): Int = R.layout.chat_row_left
    override fun getLayoutRight(): Int = R.layout.chat_row_right

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = dataset[position].sender.nickname.trim()
        holder.message.text = dataset[position].content.trim()
    }
}