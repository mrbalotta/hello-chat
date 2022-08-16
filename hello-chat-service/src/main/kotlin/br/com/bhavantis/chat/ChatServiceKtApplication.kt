package br.com.bhavantis.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ChatServiceKtApplication

fun main(args: Array<String>) {
	runApplication<ChatServiceKtApplication>(*args)
}
