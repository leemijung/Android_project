import com.google.gson.annotations.SerializedName
import java.util.*

data class ChatCompletionResponse(
    @SerializedName("id") val id: String,
    @SerializedName("object") val objectValue: String,
    @SerializedName("created") val created: Long,
    @SerializedName("model") val model: String,
    @SerializedName("system_fingerprint") val systemFingerprint: String,
    @SerializedName("choices") val choices: List<Choice>,
    @SerializedName("usage") val usage: Usage
)

data class Choice(
    @SerializedName("message") val message: Message,
    @SerializedName("index") val index: Int,
    @SerializedName("logprobs") val logprobs: Object, // logprobs가 null일 수도 있음
    @SerializedName("finish_reason") val finishReason: String
)

data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)