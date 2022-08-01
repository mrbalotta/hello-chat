package br.com.bhavantis.chatroom.infrastructure.parsing

import br.com.bhavantis.jinko.di.core.Component
import br.com.bhavantis.jinko.di.core.Single
import com.google.gson.Gson
import java.lang.reflect.Type

@Component @Single
internal class GsonWrapper(
    private val gson: Gson
): JsonParser {
    override fun <T> fromJson(value: String, cls: Class<T>): T {
        return gson.fromJson(value, cls)
    }

    override fun <T> fromJson(value: String, cls: Type): T {
        return gson.fromJson(value, cls)
    }

    override fun <T> toJson(value: T): String {
        return gson.toJson(value)
    }
}