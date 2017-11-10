package ru.vassuv.blixr.utils.ATLibriry
//
//import okhttp3.OkHttpClient
//import retrofit2.Call
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import retrofit2.http.*
//
//
//val RETROFIT: IRequest by lazy({
//    retrofit2.Retrofit.Builder()
//            .baseUrl(IRequest.Urls.BASE)
//            .client(OkHttpClient
//                    .Builder()
//                    .followRedirects(true)
//                    .addNetworkInterceptor {
//                        val request = it.request().newBuilder().build()
//                        Logger.trace("===> SERVER", "url =", request.url())
//                        Logger.trace("===> SERVER", "method =", request.method())
//                        Logger.trace("===> SERVER", "headers =", request.headers()
//                                .toMultimap()
//                                .map { "\n            ${it.key}:${it.value.toList().first()}" }
//                                .joinToString(prefix = "", postfix = ""))
//                        val response = it.proceed(request)
//                        Logger.trace("<<<< SERVER", response)
//                        response
//                    }.build())
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .build()
//            .create(IRequest::class.java)
//})
//
//interface IRequest {
//    object Urls {
//        val BASE = "https://google.com"
//    }
//
//    @GET
//    fun get(@Url url: String): Call<String>
//
//    @GET
//    fun get(@Url url: String, @QueryMap(encoded = true) params: Map<String, String>): Call <String>
//
//    @POST
//    @Headers("Content-Type: application/json")
//    fun post(@Url url: String, @Body params: String): Call<String>
//
//    @FormUrlEncoded
//    @POST
//    fun post(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>): Call<String>
//}