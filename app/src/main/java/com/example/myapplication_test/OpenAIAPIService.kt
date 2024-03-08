import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OpenAIAPIService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer "
    )
    @POST("chat/completions")
    suspend fun getCompletion(@Body requestBody: ChatRequest): Response<ChatCompletionResponse>
}

interface TMDBService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbSearchResponse>
}

/* 이전코드 - 쓰면 안됨
interface OpenAIAPIService {
    @Headers("Content-Type: application/json", "Authorization: Bearer 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")
    @POST("chat/completions")
    fun getCompletion(@Body requestBody: ChatRequest): Call<ChatCompletionResponse>
}

interface TMDBService {
    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Call<TmdbSearchResponse>
    //fun searchMovies(@Body requestBody: TmdbSearchRequest): Call<TmdbSearchResponse>
}

 */