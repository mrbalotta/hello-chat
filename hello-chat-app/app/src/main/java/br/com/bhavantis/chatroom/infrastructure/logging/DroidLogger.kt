package br.com.bhavantis.chatroom.infrastructure.logging

sealed class LogLevel
object ErrorsOnly : LogLevel()
object ErrorsOrWarningsOnly : LogLevel()
object Verbose : LogLevel()
object Silent : LogLevel()

interface DroidLogger {
    fun setLevel(level: LogLevel)
    fun error(tag: String = "", msg: String = "", throwable: Throwable)
    fun warn(tag: String = "", msg: String)
    fun info(tag: String = "", msg: String)
}