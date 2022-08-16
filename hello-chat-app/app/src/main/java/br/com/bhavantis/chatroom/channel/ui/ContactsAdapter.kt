package br.com.bhavantis.chatroom.channel.ui

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.bhavantis.chatroom.R
import br.com.bhavantis.chatroom.core.model.User

class ContactsAdapter(
    private val listener: ContactSelectionListener,
    private var dataset: MutableList<User> = mutableListOf()
): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private val colors = ContactColors()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val avatar: TextView
        val username: TextView
        val name: TextView
        val status: View
        val bar: View
        val card: CardView

        init {
            card = view.findViewById(R.id.card_row)
            avatar = view.findViewById(R.id.avatar)
            username = view.findViewById(R.id.username)
            name = view.findViewById(R.id.nickname)
            status = view.findViewById(R.id.status)
            bar = view.findViewById(R.id.contact_item_bar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setStatusColor(holder.status, dataset[position])
        setContactColors(holder, position)
        setInfos(holder, position)
        setListener(holder.card, position)
        setCalling(holder, dataset[position])
    }

    private fun setCalling(holder: ViewHolder, user: User) {
        if(user.sentMessage) {
            holder.name.setTypeface(null, Typeface.BOLD_ITALIC)
            holder.username.setTypeface(null, Typeface.BOLD_ITALIC)
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL)
            holder.username.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun setListener(card: CardView, position: Int) {
        card.setOnClickListener { listener.onContactSelected( dataset[position] ) }
    }

    private fun setContactColors(holder: ViewHolder, position: Int) {
        val color = colors.getColorRes(position)
        with(holder) {
            //bar.setBackgroundResource(color)
            setDrawableColor(avatar, color)
        }
    }

    private fun setInfos(holder: ViewHolder, position: Int) {
        val contact = dataset[position]
        with(holder) {
            avatar.text = contact.nickname.first().toString()
            username.text = contact.id
            name.text = contact.nickname
        }
    }

    override fun getItemCount() = dataset.size

    fun addContact(contact: User) {
        val filteredList = dataset.filter {
            it.id == contact.id
        }
        if(filteredList.isNotEmpty()) {
            val contactInList = filteredList.first()
            contactInList.sentMessage = contact.sentMessage
            contactInList.online = contact.online
        } else {
            dataset.add(contact)
        }
        Log.d("ALE", "adapter received ${contact.nickname}")
        notifyDataSetChanged()
    }

    fun addAll(contacts: List<User>?) {
        if(contacts == null) return
        dataset = contacts.toMutableList()
        notifyDataSetChanged()
    }

    private fun setStatusColor(view: View, contact: User) {
        val color = if(contact.online) R.color.contact_online else R.color.contact_offline
        setDrawableColor(view, color)
    }

    private fun setDrawableColor(view: View, color: Int) {
        val drawable = AppCompatResources.getDrawable(view.context, R.drawable.avatar_initial_logo_style)!!
        val compat = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(compat, ContextCompat.getColor(view.context, color))
        DrawableCompat.setTintMode(compat, PorterDuff.Mode.SRC_OVER)
        view.background = compat
    }
}