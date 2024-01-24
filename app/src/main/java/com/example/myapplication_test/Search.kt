package com.example.myapplication_test

import ChatCompletionResponse
import android.content.Intent
import android.os.Bundle
import android.os.Message
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

    var messageList: List<Message>? = null

    val et_msg = findViewById<EditText>(R.id.et_msg)
    var question :String? = null



    val JSON : MediaType= "application/json".toMediaType() //; charset=utf-8
    var client = OkHttpClient()


    //깃 올리기 전 잠금 해야 함
    private val MY_SECRET_KEY = "" //키값
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        back_button.setOnClickListener {
            startActivity(Intent(applicationContext, Selection::class.java))
            finish()
        }

        //연결시간 설정. 60초/120초/60초
        //딜레이 오류 발생 제거 목적
        client = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        
        messageList = ArrayList()

        //메일 기능 실행
        go_button.setOnClickListener {

            question = et_msg?.text.toString()  //.trim() 빼버림
            et_msg.setText("")
            callAPI(question!!)

        }

    }


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
        val body: RequestBody = json_object.toString().toRequestBody(JSON)

        //http요청 생성
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $MY_SECRET_KEY")
            .post(body)
            .build()

        //응답 json을 문자열 형태로 받나?
        client.newCall(request).enqueue(object : Callback<ChatCompletionResponse> {

            override fun onResponse(call: Call<ChatCompletionResponse>, response: Response<ChatCompletionResponse>) {
                if (response.isSuccessful) { //성공
                    try {

                        val jsonObject = JSONObject(response.body().toString())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        //body()!!.name
                        val result: String = jsonArray.getJSONObject(0).getJSONObject("message").getString("content") //결과내용
                        Log.d("log", result)

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
        })
    }
    
    private fun handleSuccess(responseBody: String) {
        //성공적인 응답 처리
        println("Successful response: $responseBody")
    }

    private fun handleFailure(errorMessage: String?) {
        //실패한 응답 처리
        println("Error: $errorMessage")
    }


}