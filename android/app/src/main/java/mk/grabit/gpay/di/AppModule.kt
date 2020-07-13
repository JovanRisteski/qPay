package mk.grabit.gpay.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import mk.grabit.gpay.BuildConfig
import mk.grabit.gpay.data.repository.MainRepository
import mk.grabit.gpay.networking.BPayService
import mk.grabit.gpay.util.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMainRepository() = MainRepository()

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()

    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideBPayService(
        okHttpClient: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ) = provideService(okHttpClient, converterFactory, BPayService::class.java)

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        clazz: Class<T>
    ): T {
        return createRetrofit(okHttpClient, converterFactory).create(clazz)
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}