package ru.mironov.appwithmapsample.core.utils.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T : Any> asResource(block: suspend () -> T): Flow<Resource<T>> = flow {
    emit(Resource.Loading())

    try {
        val result = block()
        emit(Resource.Success(result))
    } catch (e: Throwable) {
        emit(Resource.Error(e))
    }
}

fun <T : Any> Flow<T>.asResource(): Flow<Resource<T>> = map { (Resource.Success(it)) }

fun <T : Any, R : Any> Resource<T>.map(block: (value: T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(block(value))
        is Resource.Loading -> Resource.Loading()
        is Resource.Error -> Resource.Error(throwable)
    }
}