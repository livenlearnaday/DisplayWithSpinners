package io.github.livenlearnaday.countrylistkotlin.data.remote



import io.github.livenlearnaday.displaywithspinners.data.remote.ApiService
import io.github.livenlearnaday.displaywithspinners.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {


        var client = OkHttpClient().newBuilder()
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.MINUTES).build()



    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val API_SERVICE: ApiService = getRetrofit().create(ApiService::class.java)

}
