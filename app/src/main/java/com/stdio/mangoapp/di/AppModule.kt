package com.stdio.mangoapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.stdio.mangoapp.common.TokenManager
import com.stdio.mangoapp.data.AuthAuthenticator
import com.stdio.mangoapp.data.AuthInterceptor
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.data.MainService
import com.stdio.mangoapp.data.RemoteDataSource
import com.stdio.mangoapp.domain.usecases.CheckAuthCodeUseCase
import com.stdio.mangoapp.presentation.viewmodel.AuthViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import com.stdio.mangoapp.presentation.viewmodel.RegistrationViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}

val appModule = module {
    fun providesBaseUrl(): String = "https://plannerok.ru/api/v1/"

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    fun provideTokenManager(context: Context) = TokenManager(context)

    fun provideAuthInterceptor(tokenManager: TokenManager) = AuthInterceptor(tokenManager)

    fun provideAuthenticator(tokenManager: TokenManager) = AuthAuthenticator(tokenManager)

    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, tokenManager: TokenManager): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(AuthInterceptor(tokenManager))
            .authenticator(AuthAuthenticator(tokenManager))
            .addInterceptor(httpLoggingInterceptor)
    }

    fun provideRetrofit(BASE_URL: String, httpBuilder: OkHttpClient.Builder): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpBuilder.build())
            .build()

    fun provideMainService(retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)

    fun provideRemoteDataSource(mainService: MainService) =
        RemoteDataSource(mainService)

    fun provideRepository(remoteDataSource: RemoteDataSource) =
        MainRepository(remoteDataSource)

    fun provideCheckAuthCodeUseCase(repository: MainRepository) = CheckAuthCodeUseCase(repository)

    single { providesBaseUrl() }
    single {provideHttpLoggingInterceptor()}
    single { provideTokenManager(get()) }
    single { provideAuthInterceptor(get()) }
    single { provideAuthenticator(get()) }
    factory { provideHttpClient(get(), get()) }
    single { provideRetrofit(get(), get()) }
    single { provideMainService(get()) }
    single { provideRemoteDataSource(get()) }
    single { provideRepository(get()) }
    single { provideCheckAuthCodeUseCase(get()) }
}

val allModules = viewModelModule + appModule