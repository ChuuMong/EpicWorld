package com.chuumong.games.domain.usecase

import kotlinx.coroutines.*


abstract class BaseUseCase<REQUEST, RESPONSE> {

    operator fun invoke(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        request: REQUEST,
        onResponse: (RESPONSE?) -> Unit
    ) {
        val job = scope.async(dispatcher) {
            run(request = request)
        }

        scope.launch(dispatcher) {
            if (isActive) {
                try {
                    onResponse(job.await())
                } catch (e: Exception) {
                    onResponse(null)
                }
            }
        }
    }

    abstract suspend fun run(request: REQUEST): RESPONSE

}