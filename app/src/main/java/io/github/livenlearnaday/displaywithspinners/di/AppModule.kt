package io.github.livenlearnaday.displaywithspinners.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.livenlearnaday.displaywithspinners.data.entity.SiteDataDao
import io.github.livenlearnaday.displaywithspinners.data.entity.UnitDataDao
import io.github.livenlearnaday.displaywithspinners.data.local.database.AppDatabase
import io.github.livenlearnaday.displaywithspinners.data.local.key_value_pairs.SharedPrefHelper
import io.github.livenlearnaday.displaywithspinners.data.remote.ApiHelper
import io.github.livenlearnaday.displaywithspinners.data.remote.ApiService
import io.github.livenlearnaday.displaywithspinners.data.repository.SharedPrefHelperRepository
import io.github.livenlearnaday.displaywithspinners.data.repository.SiteDataRepository
import io.github.livenlearnaday.displaywithspinners.data.repository.UnitDataRepository
import io.github.livenlearnaday.displaywithspinners.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)




    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)


    @Provides
    fun provideSiteDataDao(db: AppDatabase) = db.siteDataDao()


    @Provides
    fun provideUnitDataDao(db: AppDatabase) = db.unitDataDao()



    @Singleton
    @Provides
    fun provideRepositorySiteData(
        apiHelper: ApiHelper,
        siteDataDao: SiteDataDao,
    ) = SiteDataRepository(apiHelper, siteDataDao)


    @Singleton
    @Provides
    fun provideRepositoryUnitData(
        apiHelper: ApiHelper,
        unitDataDao: UnitDataDao
    ) = UnitDataRepository(apiHelper, unitDataDao)


    @Singleton
    @Provides
    fun provideRepositorySharedPrefHelper(
        sharedPrefHelper: SharedPrefHelper
    ) = SharedPrefHelperRepository(sharedPrefHelper)


}
