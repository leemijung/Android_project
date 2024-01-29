import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIAPIService {
    @Headers("Content-Type: application/json", "Authorization: Bearer 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")
    @POST("chat/completions")
    fun getCompletion(@Body requestBody: ChatRequest): Call<ChatCompletionResponse>
}