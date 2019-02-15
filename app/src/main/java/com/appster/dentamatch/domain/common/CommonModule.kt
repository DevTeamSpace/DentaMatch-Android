package com.appster.dentamatch.domain.common

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): CommonRetrofit =
            retrofit.create(CommonRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepository(commonRetrofit: CommonRetrofit,
                                composer: RemoteRepositoryComposer): CommonRemoteRepository =
            CommonRemoteRepository(commonRetrofit, composer)

    @Provides
    @Singleton
    fun provideInteractor(commonRemoteRepository: CommonRemoteRepository): CommonInteractor =
            CommonInteractor(commonRemoteRepository)
}