package br.com.bhavantis.chat.controller

import br.com.bhavantis.chat.model.ChatMessage
import br.com.bhavantis.chat.model.User
import br.com.bhavantis.chat.service.ChatIdGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val idGenerator: ChatIdGenerator
) {

    private val context = newSingleThreadContext("chat-controller")

    companion object {
        val users = mutableListOf<User>()
    }

    private val logger: Logger = LoggerFactory.getLogger(ChatController::class.java)

    @MessageMapping("/broadcast") //chat/broadcast
    @SendTo("/room/public")
    fun sendBroadcast(@Payload chatMessage: ChatMessage): ChatMessage {
        println("mensagem recebida: ${chatMessage.content}")
        return chatMessage
    }

    @MessageMapping("/private") //chat/private
    fun sendPrivateMessage(@Payload chatMessage: ChatMessage): ChatMessage {
        messagingTemplate.convertAndSendToUser(chatMessage.receiver.id, "/private", chatMessage) //user/{name}/private
        return chatMessage
    }

    @PostMapping("/api/contacts")
    fun createUser(@RequestBody user: User): User {
        val registration = User(
            id = idGenerator.generate(),
            nickname = user.nickname,
            online = true
        )

        CoroutineScope(context).launch {
            users.add(registration)
        }

        messagingTemplate.convertAndSend("/room/contacts", getChatMessageFromUser("/room/contacts", registration))
        return registration
    }

    @GetMapping("/api/contacts")
    fun getUsers(): List<User> {
        return users
    }

    private fun getChatMessageFromUser(topic: String, user: User) = ChatMessage(
        sender = user,
        topic = topic,
        receiver = User(id="ALL", nickname = "ALL")
    )
}