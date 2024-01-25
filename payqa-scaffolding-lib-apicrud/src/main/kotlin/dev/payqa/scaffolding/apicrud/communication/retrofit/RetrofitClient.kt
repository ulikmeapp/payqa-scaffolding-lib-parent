package dev.payqa.scaffolding.apicrud.communication.retrofit

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import dev.payqa.scaffolding.apicrud.communication.retrofit.adapter.type.UnixEpochDateAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import dev.payqa.scaffolding.apicrud.communication.retrofit.config.RetrofitConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.util.*

@Suppress("UNCHECKED_CAST")
open class RetrofitClient<T>(
    baseUrl: String,
    private val config: RetrofitConfig? = null,
    interceptors: Array<Interceptor>? = null
) {

    private var retrofit: Retrofit? = null

    protected var manager: T

    companion object {
        private val log = LoggerFactory.getLogger(RetrofitClient::class.java)
    }

    init {
        // START OF: Init retrofit instance
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request()
                it.proceed(
                    request.newBuilder()
                        .build()
                )
            }

        interceptors?.let {
            it.forEach { interceptor ->
                clientBuilder.addInterceptor(interceptor)
            }
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(logging)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()

        this.config?.let {
            if (it.dateAsLong)
                gsonBuilder.registerTypeAdapter(Date::class.java, UnixEpochDateAdapter)
        }

        this.retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(gsonBuilder.create())
            )
            .client(clientBuilder.build())
            .build()
        // END OF: Init retrofit instance

        // Init manager
        manager = this.retrofit!!.create(
            (javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<T>
        )
    }

}