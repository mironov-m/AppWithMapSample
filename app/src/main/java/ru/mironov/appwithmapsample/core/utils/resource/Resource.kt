package ru.mironov.appwithmapsample.core.utils.resource

sealed interface Resource<T : Any> {

    data class Success<T : Any>(val value: T) : Resource<T>

    class Loading<T : Any> : Resource<T>

    data class Error<T : Any>(val throwable: Throwable) : Resource<T>
}