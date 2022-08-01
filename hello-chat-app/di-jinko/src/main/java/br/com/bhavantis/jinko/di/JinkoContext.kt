package br.com.bhavantis.jinko.di

class JinkoContext(val name: String) {

    companion object {
        val contexts: MutableMap<String, Map<String, JinkoFactory>> = mutableMapOf()
        const val DEFAULT_CONTEXT = "default"

        @Suppress("UNCHECKED_CAST")
        fun <T> inject(name: String, clz: Class<T>, qualifier: String = ""): T {
            if(!contexts.containsKey(name)) throw ContextNotFoundException(clz.name)

            val mapping = resolveNaming(clz, qualifier)
            if(!contexts[name]!!.containsKey(mapping)) throw DependencyNotFoundException(mapping)

            return contexts[name]!![mapping]!!.get() as T
        }
    }

    fun provider(fn: Provider.()->Unit) = with(Provider()) {
        fn(this)
        val provider = build()
        contexts.put(name, provider)
    }

    inline fun <reified T> get(qualifier: String = ""): T {
        return inject(name, T::class.java, qualifier)
    }
}

class DependencyNotFoundException(dependency: String): RuntimeException(dependency)
class ContextNotFoundException(name: String): RuntimeException(name)