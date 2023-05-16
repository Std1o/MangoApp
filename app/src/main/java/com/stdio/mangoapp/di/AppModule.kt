package com.stdio.mangoapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.stdio.mangoapp.common.TokenManager
import com.stdio.mangoapp.data.remote.AuthAuthenticator
import com.stdio.mangoapp.data.remote.AuthInterceptor
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.data.db.ProfileDataDB
import com.stdio.mangoapp.data.db.ProfileDao
import com.stdio.mangoapp.data.remote.MainService
import com.stdio.mangoapp.data.remote.RemoteDataSource
import com.stdio.mangoapp.domain.usecases.CheckAuthCodeUseCase
import com.stdio.mangoapp.presentation.viewmodel.AuthViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileEditingViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import com.stdio.mangoapp.presentation.viewmodel.RegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
    viewModel { ProfileEditingViewModel(get()) }
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

    fun provideAppDatabase(context: Context) = ProfileDataDB.getDatabase(context, CoroutineScope(SupervisorJob()))

    fun provideDao(database: ProfileDataDB) = database.profileDataDao()

    fun provideRepository(remoteDataSource: RemoteDataSource, profileDao: ProfileDao) =
        MainRepository(remoteDataSource, profileDao)

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
    single { provideAppDatabase(get()) }
    single { provideDao(get()) }
    single { provideRepository(get(), get()) }
    single { provideCheckAuthCodeUseCase(get()) }
}

val allModules = viewModelModule + appModule