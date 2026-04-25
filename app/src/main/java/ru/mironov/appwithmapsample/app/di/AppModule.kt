package ru.mironov.appwithmapsample.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mironov.appwithmapsample.core.android.InternetConnectionObserver
import ru.mironov.appwithmapsample.core.android.InternetConnectionObserverImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindInternetConnectionObserver(
        impl: InternetConnectionObserverImpl
    ): InternetConnectionObserver
}