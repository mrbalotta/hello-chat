package br.com.bhavantis.jinko.di

fun jinkoContext(name: String = JinkoContext.DEFAULT_CONTEXT, fn: JinkoContext.()->Unit): JinkoContext = JinkoContext(name).apply {
    fn(this)
}

inline fun <reified T> get(qualifier: String = ""): T {
    return JinkoContext.inject(JinkoContext.DEFAULT_CONTEXT, T::class.java, qualifier)
}

inline fun <reified T> inject(name: String = JinkoContext.DEFAULT_CONTEXT, qualifier: String = "") =
    lazy { JinkoContext.inject(name, T::class.java, qualifier) }