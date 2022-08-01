package br.com.bhavantis.chatroom.infrastructure.logging

import android.util.Log

class DefaultLogger: DroidLogger {
    private var level: LogLevel = Verbose

    override fun setLevel(level: LogLevel) {
        this.level = level
    }

    override fun error(tag: String, msg: String, throwable: Throwable) {
        if(level != Silent) Log.e(tag, msg, throwable)
    }

    override fun warn(tag: String, msg: String) {
        if(level != ErrorsOnly && level != Silent) Log.w(tag, msg)
    }

    override fun info(tag: String, msg: String) {
        if(level == Verbose) Log.i(tag, msg)
    }
}