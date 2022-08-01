package br.com.bhavantis.chatroom.channel.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bhavantis.chatroom.core.model.ChatMessage

internal abstract class AbstractChatAdapter<T:RecyclerView.ViewHolder>(
    protected val dataset: MutableList<ChatMessage>
): RecyclerView.Adapter<T>() {

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }

    protected fun getLayout(viewType: Int): Int {
        return if(viewType == LEFT) getLayoutLeft() else getLayoutRight()
    }

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater
            .from(parent.context)
            .inflate(getLayout(viewType), parent, false)
    }

    protected abstract fun getLayoutLeft(): Int
    protected abstract fun getLayoutRight(): Int

    override fun getItemCount() = dataset.size

    override fun getItemViewType(position: Int): Int {
        if (dataset[position].isSelf()) return RIGHT
        return LEFT
    }

    fun addMessage(message: ChatMessage) {
        dataset.add(message)
        notifyItemInserted(dataset.size - 1)
    }
}