package br.com.bhavantis.chatroom.channel.ui

import br.com.bhavantis.chatroom.R


class ContactColors {

    private val colors = arrayOf(
        R.color.avatar_01,
        R.color.avatar_02,
        R.color.avatar_03,
        R.color.avatar_04,
        R.color.avatar_05,
        R.color.avatar_06
    )

    fun getColorRes(position: Int): Int {
        return colors[ position % colors.size ]
    }
}