package br.com.bhavantis.chatroom.infrastructure.messaging

import br.com.bhavantis.chatroom.infrastructure.logging.DroidLogger
import br.com.bhavantis.chatroom.infrastructure.parsing.JsonParser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.rx2.asCoroutineDispatcher
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

class StompMessagingBroker(
    private val url: String,
    private val parser: JsonParser,
    private val logger: DroidLogger
): MessagingBroker {
    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
    private val compositeDisposable = CompositeDisposable()
    private val observerContext = Schedulers.single()

    override fun connect(listener: ConnectionListener) {
        logger.info("ICHAT", "running 'StompMessagingBroker.connect' on: ${Thread.currentThread().name}")
        logger.info("ICHAT", "try to connect to $url")
        prepareClient(listener)
        stompClient.connect()
    }

    private fun prepareClient(listener: ConnectionListener) {
        val disposable = stompClient
            .lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(observerContext)
            .subscribe { event -> handleStompLifecycleEvent(event, listener) }
        compositeDisposable.add(disposable)
    }

    private fun handleStompLifecycleEvent(event: LifecycleEvent, listener: ConnectionListener) {
        logger.info("ICHAT", "running 'StompMessagingBroker.handleStompLifecycleEvent' on: ${Thread.currentThread().name}")
        when (event.type!!) {
            LifecycleEvent.Type.CLOSED -> { listener.onClosed(this) }
            LifecycleEvent.Type.ERROR -> { listener.onFailed(this, event.exception) }
            LifecycleEvent.Type.OPENED -> { listener.onOpened(this) }
            LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> { listener.onHeartbeatFailed(this, event.exception) }
        }
    }

    override fun disconnect() {
        logger.info("ICHAT", "running 'StompMessagingBroker.disconnect' on: ${Thread.currentThread().name}")
        stompClient.disconnect()
    }

    override fun send(destination: String, message: Any) {
        logger.info("ICHAT", "running 'StompMessagingBroker.send' on: ${Thread.currentThread().name}")
        val disposable = stompClient
            .send(destination, parser.toJson(message))
            .subscribeOn(Schedulers.io())
            .subscribe() {
                logger.info("ICHAT", "running 'StompMessagingBroker.send (subscribe)' on: ${Thread.currentThread().name}")
            }
        compositeDisposable.add(disposable)
    }

    override fun subscribe(topic: String, listener: MessagingListener) {
        logger.info("ICHAT", "running 'StompMessagingBroker.subscribe' on: ${Thread.currentThread().name}")
        val disposable = stompClient
            .topic(topic)
            .subscribe { listener.onMessage(topic, it.payload) }
        compositeDisposable.add(disposable)
    }

    override fun getDispatcher(): CoroutineDispatcher {
        return observerContext.asCoroutineDispatcher()
    }
}