import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OpenAIAPIService {
    @Headers("Content-Type: application/json", "Authorization: Bearer ")
    @POST("chat/completions")
    fun getCompletion(@Body requestBody: ChatRequest): Call<ChatCompletionResponse>
}

interface TMDBService {
    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Call<TmdbSearchResponse>
    //fun searchMovies(@Body requestBody: TmdbSearchRequest): Call<TmdbSearchResponse>
}