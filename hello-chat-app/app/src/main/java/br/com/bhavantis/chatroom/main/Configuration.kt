package br.com.bhavantis.chatroom.main

import android.util.Log
import br.com.bhavantis.chatroom.BuildConfig
import br.com.bhavantis.chatroom.channel.domain.ContactsRepository
import br.com.bhavantis.chatroom.channel.domain.PrivateRoomRepository
import br.com.bhavantis.chatroom.infrastructure.logging.DefaultLogger
import br.com.bhavantis.chatroom.infrastructure.logging.DroidLogger
import br.com.bhavantis.chatroom.infrastructure.logging.Silent
import br.com.bhavantis.chatroom.infrastructure.messaging.MessagingBroker
import br.com.bhavantis.chatroom.infrastructure.messaging.StompMessagingBroker
import br.com.bhavantis.chatroom.main.repository.ApiConfig
import br.com.bhavantis.chatroom.main.repository.UserRepositoryImpl
import br.com.bhavantis.chatroom.registration.domain.UserRegistrationRepository
import br.com.bhavantis.jinko.di.core.Bean
import br.com.bhavantis.jinko.di.core.Provider
import br.com.bhavantis.jinko.di.core.Single
import br.com.bhavantis.jinko.di.get
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

@Provider
class Configuration {

    @Bean
    @Single
    fun getApiConfig() = object: ApiConfig {
        override fun getUrl(): String {
            return "http://10.0.2.2:9090/api"
        }
    }

    @Bean
    fun createLogger(): DroidLogger {
        val logger = DefaultLogger()
        if(!BuildConfig.DEBUG) logger.setLevel(Silent)
        return logger
    }

    @Bean
    @Single
    fun createMessagingBroker(): MessagingBroker {
        val channel = StompMessagingBroker("http://10.0.2.2:9090/app/websocket", get(), get())
        return channel
    }

    @Bean
    fun getUserRegistrationRepository(): UserRegistrationRepository = UserRepositoryImpl

    @Bean
    fun getContactsRepository(): ContactsRepository = UserRepositoryImpl

    @Bean
    fun getPrivateRoomRepository(): PrivateRoomRepository = UserRepositoryImpl

    @Bean @Single
    fun gsonBuilder(): Gson = GsonBuilder().create()

    @Bean @Single
    fun getHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.ALL
                logger = CustomLogger
            }
            install(ContentNegotiation) {
                gson()
                //or serializer = KotlinxSerializer()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }
            defaultRequest {
                // Parameter("api_key", "some_api_key")
                // Content Type
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }

    private object CustomLogger: Logger {
        override fun log(message: String) {
            Log.i("ALE", message)
        }
    }
}