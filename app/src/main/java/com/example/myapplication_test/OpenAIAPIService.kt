import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIAPIService {
    @Headers("Content-Type: application/json", "Authorization: Bearer **여기가 api키 넣으면 됩니다**")
    @POST("chat/completions")
    fun getCompletion(@Body requestBody: ChatRequest): Call<ChatCompletionResponse>
}