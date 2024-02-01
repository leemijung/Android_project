package com.example.myapplication_test

import ChatCompletionResponse
import ChatMessage
import ChatRequest
import TmdbSearchResponse
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


    class MainLogic(question: String) {

        val messages = listOf(
            ChatMessage(role = "user", content = question)
        )

        val request = ChatRequest(
            messages = messages,
            model = "gpt-3.5-turbo"
        )

        // API와 통신하는 함수
        fun communicateWithAPI(requestData: GPTRequest) {
            // GPT-3.5 Turbo API 요청
            RetrofitClient.instance.getCompletion(request).enqueue(object : Callback<ChatCompletionResponse> {
                override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        // gpt 응답 값 전체 출력
                        Log.d("MyTag", result.toString())

                        val movieTitles = result?.choices!![0].message.content.split("\n")
                            .map { it.substringAfter(". ").trim() } // 각 줄에서 번호와 영화 제목을 분리하고 영화 제목만 추출
                            .toTypedArray()

                        //val movieTitlesString = movieTitles.joinToString(", ")
                        //Log.d("MyTag", movieTitlesString)


                        // **tmdb api 요청 코드**
                        for (query in movieTitles) {
                            communicateWithTM(query)
                        }


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




        // TMDB API 요청
        fun communicateWithTM(query: String){


            val includeAdult = false
            val language = "en-US"
            val page = 1
            val apiKey = "0e209ce1c01b25eda1f1d3b69327e72a"

            RetrofitClient.instance_2.searchMovies(query, includeAdult, language, page, apiKey).enqueue(object : Callback<TmdbSearchResponse> {
                override fun onResponse(call: Call<TmdbSearchResponse>, response: Response<TmdbSearchResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        // gpt 응답 값 전체 출력
                        Log.d("MyTag", result.toString())



                        // **tmdb 응답 값 처리코드 작성해야 함**


                    } else {
                        // 실패 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.d("MyTag", "Failed: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<TmdbSearchResponse>, t: Throwable) {
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

            // question: 사용자가 직접 입력한 문장
            val question = et_msg?.text.toString()
            et_msg.setText("")
            val appendedText = "아무런 코멘트 없이 오로지 영화제목만 10개 말해줘."
            val questionText = "$question $appendedText"

            Log.d("MyTag", questionText)


            // API와 통신
            val mainLogic = MainLogic(questionText)
            mainLogic.communicateWithAPI(requestData)
            startActivity(Intent(applicationContext, Results_display::class.java))


        }

    }

}