package br.com.bhavantis.jinko.di

import java.lang.ref.WeakReference

class Provider(private val map: MutableMap<String, JinkoFactory> = mutableMapOf()) {

    inline fun <reified T> single(qualifier: String = "", noinline factory: ()->T) {
        val mapping = resolveNaming(T::class.java, qualifier)
        add(mapping, JinkoSingle(lazy { WeakReference(factory.invoke()) } ))
    }

    inline fun <reified T> factory(qualifier: String = "", noinline factory: ()->T) {
        val mapping = resolveNaming(T::class.java, qualifier)

        add(mapping, object : JinkoFactory {
            override fun get(): Any? {
                return factory.invoke()
            }
        })
    }

    fun add(mapping: String, factory: JinkoFactory) {
        if(!map.containsKey(mapping)) map[mapping] = factory
    }

    internal fun build() = map.toMap()
}