package com.example.commyproject.di

import com.taymay.taoday.service.SocketIOManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketIoModule {
//    private val TRANSPORTS = arrayOf(WebSocket.NAME)
    @Provides
    @Singleton
    fun provideSocketIO() : Socket {
        val socket : Socket
//        val option = IO.Options.builder()
//            .setTransports(TRANSPORTS)
//            .setUpgrade(true)
//            .setReconnection(false)
//            .build()
        val option = IO.Options()
        option.reconnection = true
//        option.transports = TRANSPORTS
        option.upgrade = true
        option.reconnectionDelay = 1000
        option.timeout = 10000
        socket =
            IO.socket(
                SocketIOManager.SERVER_URL,option
            )
        return socket
    }

}