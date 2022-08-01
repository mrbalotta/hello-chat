package br.com.bhavantis.jinko.di

import java.lang.ref.WeakReference

class JinkoSingle<T>(private val holder: Lazy<WeakReference<T>>): JinkoFactory {
    override fun get(): Any? {
        return holder.value.get()
    }
}