package br.com.bhavantis.chatroom.infrastructure.parsing

import java.lang.reflect.Type

interface JsonParser {
    fun <T> fromJson(value: String, cls: Class<T>): T
    fun <T> fromJson(value: String, cls: Type): T
    fun <T> toJson(value: T): String
}