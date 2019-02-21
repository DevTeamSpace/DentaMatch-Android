package com.appster.dentamatch.domain.common

import com.appster.dentamatch.base.RemoteRepositoryComposer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CommonModule {

    @Provides
    fun provideRetrofit(retrofit: Retrofit): CommonRetrofit =
            retrofit.create(CommonRetrofit::class.java)

    @Provides
    fun provideRemoteRepository(commonRetrofit: CommonRetrofit,
                                composer: RemoteRepositoryComposer): CommonRemoteRepository =
            CommonRemoteRepository(commonRetrofit, composer)

    @Provides
    fun provideInteractor(commonRemoteRepository: CommonRemoteRepository): CommonInteractor =
            CommonInteractor(commonRemoteRepository)
}