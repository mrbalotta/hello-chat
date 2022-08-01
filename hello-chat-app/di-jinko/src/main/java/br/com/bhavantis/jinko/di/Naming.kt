package br.com.bhavantis.jinko.di

fun <T> resolveNaming(clz: Class<T>, qualifier: String): String {
    return clz.name + "@" + qualifier
}