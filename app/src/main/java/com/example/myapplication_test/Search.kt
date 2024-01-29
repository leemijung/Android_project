package com.example.myapplication_test

import ChatCompletionResponse
import ChatMessage
import ChatRequest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.search.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.sql.DriverManager.println
import java.util.*
import java.util.concurrent.TimeUnit

class Search  : AppCompatActivity() {


    //var et_msg: EditText? = null //사용자 입력창
    //var go_button: TextView? = null  //go 버튼

    //var messageList: List<Message>? = null

    //val et_msg = findViewById<EditText>(R.id.et_msg)
    //var question :String? = null



    //val JSON = "application/json; charset=utf-8".toMediaType()

    //연결시간 설정. 60초/120초/60초
    //딜레이 오류 발생 제거 목적
/*    var client = OkHttpClient().newBuilder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(120, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

    //깃 올리기 전 잠금 해야 함
    private val MY_SECRET_KEY = "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"

*/
    //여기부터
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
                        // API 응답을 처리합니다.
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

            //question = et_msg?.text.toString()  //.trim() 빼버림
            //et_msg.setText("")
            //callAPI(question!!)

            // GPT-3.5 Turbo API와 통신
            val mainLogic = MainLogic()
            mainLogic.communicateWithGPT(requestData)
            startActivity(Intent(applicationContext, Results_display::class.java))



        }

    }


/*
    //이 아래는 아님

    //okhttp로 gpt api 연결
    private fun callAPI(question:String){

        val arr = JSONArray()
        val baseAi = JSONObject()
        val userMsg = JSONObject()

        try {
            //AI 속성설정
            baseAi.put("role", "user")
            baseAi.put("content", "You are a AI Assistant.")

            //유저 메세지
            userMsg.put("role", "user")
            userMsg.put("content", question)

            //array로 담아서 한번에 보낸다
            arr.put(baseAi)
            arr.put(userMsg)

        } catch (e: JSONException) {
            throw RuntimeException(e)
        }


        //json객체 생성
        val json_object= JSONObject()

        try {
            json_object.put("messages", arr)
            json_object.put("model", "gpt-3.5-turbo")

        } catch (e: JSONException){
            e.printStackTrace()
        }


        //요청바디 생성
        val body = json_object.toString().toRequestBody(JSON)

        //http요청 생성
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $MY_SECRET_KEY")
            .post(body)
            .build()


        //응답
        /*client.newCall(request).enqueue(object : Callback<ChatCompletionResponse>{

            override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                if (response.isSuccessful) { //성공
                    try {

                        val gson = Gson()
                        val type = object : TypeToken<ChatCompletionResponse>() {}.type
                        val chatCompletionResponse = gson.fromJson<ChatCompletionResponse>(response.body()?.toString(), type)
                        val jsonArray = JSONArray(chatCompletionResponse.choices)
                        val result: String = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                        Log.d("log", result)


                        //val chatCompletionResponse = response.body()
                        //val jsonObject = JSONObject(chatCompletionResponse.toString())
                        //val jsonArray = jsonObject.getJSONArray("choices")
                        //val result: String = jsonArray.getJSONObject(0).getJSONObject("message").getString("content") //결과내용
                        //Log.d("log", result)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else { //실패
                    Log.d("log", response.body().toString())
                }
            }
            override fun onFailure(call: Call<ChatCompletionResponse>, t: Throwable?) { //실패
                t?.printStackTrace()
            }
        })*/
    }


    private fun handleFailure(errorMessage: String?) {
        //실패한 응답 처리
        println("Error: $errorMessage")
    }
*/

}