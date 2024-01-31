import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIAPIService {
    @Headers("Content-Type: application/json", "Authorization: Bearer ")
    @POST("chat/completions")
    fun getCompletion(@Body requestBody: ChatRequest): Call<ChatCompletionResponse>
}