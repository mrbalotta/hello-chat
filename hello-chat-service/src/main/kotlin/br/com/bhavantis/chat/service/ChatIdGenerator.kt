package br.com.bhavantis.chat.service

interface ChatIdGenerator {
    fun generate(): String
}