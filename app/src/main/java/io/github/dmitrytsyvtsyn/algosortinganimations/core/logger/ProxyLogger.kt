package io.github.dmitrytsyvtsyn.algosortinganimations.core.logger

import core.data.logger.AndroidLogcatLogger

object ProxyLogger : Logger {

    private var realLogger: Logger = AndroidLogcatLogger()

    fun setLogger(logger: Logger) {
        realLogger = logger
    }

    override fun debug(tag: String, message: String) {
        realLogger.debug(tag, message)
    }

}