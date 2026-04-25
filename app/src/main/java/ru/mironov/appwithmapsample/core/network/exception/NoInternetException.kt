package ru.mironov.appwithmapsample.core.network.exception

class NoInternetException(cause: Throwable? = null) :
    Exception("Нет подключения к интернету", cause)