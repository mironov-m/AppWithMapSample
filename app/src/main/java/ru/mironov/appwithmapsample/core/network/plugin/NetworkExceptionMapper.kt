package ru.mironov.appwithmapsample.core.network.plugin

import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import ru.mironov.appwithmapsample.core.network.exception.NoInternetException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val NetworkExceptionMapper = createClientPlugin("NetworkExceptionMapper") {
    on(Send) { request ->
        try {
            proceed(request)
        } catch (e: UnknownHostException) {
            throw NoInternetException(e)
        } catch (e: ConnectException) {
            throw NoInternetException(e)
        } catch (e: SocketTimeoutException) {
            throw NoInternetException(e)
        } catch (e: IOException) {
            throw NoInternetException(e)
        }
    }
}