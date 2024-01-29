package com.example.myapplication_test

import ChatCompletionResponse
import ChatMessage
import ChatRequest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.search.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class Search  : AppCompatActivity() {

    // GPT-3.5 Turbo API 요청에 필요한 데이터 모델
    data class GPTRequest(
        val model: String,
        val prompt: String,
        val maxTokens: Int
    )
    class MainLogic {

        val messages = listOf(
            ChatMessage(role = "user", content = "내 이름은 이미정, 내 이름을 불러줘.")
        )

        val request = ChatRequest(
            messages = messages,
            model = "gpt-3.5-turbo" // 사용할 모델 ID
        )

        // GPT-3.5 Turbo API와 통신하는 함수
        fun communicateWithGPT(requestData: GPTRequest) {
            // GPT-3.5 Turbo API 요청
            RetrofitClient.instance.getCompletion(request).enqueue(object : Callback<ChatCompletionResponse> {
                override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        // **API 응답처리 구현해야함

                        Log.d("MyTag", result.toString())

                    } else {
                        // 실패 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.d("MyTag", "Failed: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<ChatCompletionResponse>, t: Throwable) {
                    // 실패 처리
                    Log.e("MyTag", "Error: ${t.message}", t)
                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Selection::class.java))
            finish()
        }


        // GPT-3.5 Turbo API 요청 데이터 설정
        val requestData = GPTRequest(
            model = "gpt-3.5-turbo-0613",
            prompt = "Once upon a time",
            maxTokens = 50
        )

        //메인 기능 실행
        go_button.setOnClickListener {

            //question = et_msg?.text.toString()  // question: 사용자가 입력한 문장
            //et_msg.setText("")


            // GPT-3.5 Turbo API와 통신
            val mainLogic = MainLogic()
            mainLogic.communicateWithGPT(requestData)
            startActivity(Intent(applicationContext, Results_display::class.java))


        }

    }

}