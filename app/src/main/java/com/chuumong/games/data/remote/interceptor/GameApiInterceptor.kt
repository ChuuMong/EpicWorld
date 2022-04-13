package com.chuumong.games.data.remote.interceptor

import com.chuumong.games.BuildConfig
import com.chuumong.games.data.remote.exception.RemoteException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class GameApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val requestBuilder = chain.request().newBuilder()
            val originalUrl = chain.request().url
            val updatedUrl = originalUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.API_KEY).build()
            requestBuilder.url(updatedUrl)
            val response = chain.proceed(requestBuilder.build())
            if (response.code in HttpURLConnection.HTTP_BAD_REQUEST..499) {
                throw RemoteException.ClientError(response.message)
            } else if (response.code in HttpURLConnection.HTTP_INTERNAL_ERROR..599) {
                throw RemoteException.ServerError(response.message)
            }
            return response
        } catch (e: Exception) {
            throw when (e) {
                is UnknownHostException -> RemoteException.NoNetworkError(e.message.toString())
                is SocketTimeoutException -> RemoteException.NoNetworkError(e.message.toString())
                is RemoteException -> e
                else -> RemoteException.GenericError(e.message.toString())
            }
        }
    }
}