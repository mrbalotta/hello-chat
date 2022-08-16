package br.com.bhavantis.chat.service

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatUUID: ChatIdGenerator {
    override fun generate(): String {
        return UUID.randomUUID().toString()
    }
}